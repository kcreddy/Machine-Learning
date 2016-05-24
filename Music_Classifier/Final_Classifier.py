## ******  Machine Learning Project-3  *******
## Music Classification of GTZAN dataset by:
# 1) Krishna Chaitanya Reddy Burri
# 2) Jivan Patil

# Importing require packages from scipy, sklearn, pywt
import os, sys
import pickle
import numpy as np
import math
import scipy
from scipy.io import wavfile
from scikits.talkbox.features import mfcc
from sklearn.cross_validation import KFold
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression
from sklearn.decomposition import PCA, KernelPCA
from sklearn.feature_selection import SelectKBest
from sklearn.feature_selection import chi2
import pywt
from sklearn.ensemble import RandomForestClassifier
from sklearn.ensemble import AdaBoostClassifier
from sklearn.svm import SVC
from sklearn.ensemble import VotingClassifier

# Storing .wav file names into dictionary, to help reading in next step
def assignIndices():
    dict = {}
    index = 1
    for each in genres:
        for i in range(0,90):
            dict[index] = "wav_files/"+each+".000"+(str(i).zfill(2))+".wav"
            index += 1
    return dict

# Reading each .wav file. Each file name is taken from above dictionary.
# A dictionary of [sample_rate,X] for each song is returned
def readFiles():
    dict = {}
    for i in range(1,901):
        sample_rate,X = wavfile.read(dict_files.get(i))
        dict[i] = [sample_rate,X]
    return dict

# Applying FFT transformation to read samples above, and extracting first 1000 features
# Returns a dictionary containing:
#   keys --> 1,2,3,...899,900 (song labels)
#   values --> first 1000 FFT features for each song
def fftIFY(dict_read):
    dict = {}
    for each in dict_read.keys():
        [sample_rate,X] = dict_read.get(each)
        fft_features = abs(scipy.fft(X)[:1000])
        dict[each] = fft_features
    return dict

# Applying MFC transformation to read samples
# Returns a dictionary containing:
#   keys --> 1,2,3,...899,900 (song labels)
#   values --> "ceps" numpy array for each song of shape (4135,13)
def mfccIFY(dict_read):
    dict = {}
    for each in dict_read.keys():
        [sample_rate,X] = dict_read.get(each)
        ceps, mspec, spec = mfcc(X)
        dict[each] = ceps
    return dict

# Returns the averaged MFCC values across frames for each song
# 2 songs have 'NaN' values and 'Inf' values which are all replaced with '0.0'
def getAvgMfcc(dict_mfcc):
    dict = {}
    for each in dict_mfcc.keys():
        ceps = dict_mfcc.get(each)
        num_ceps = ceps.shape[0]
        x = np.mean(ceps[int(num_ceps*(1.0/10)):int(num_ceps*(9.0/10))],axis=0)
        x_inf = np.isinf(x)
        x[x_inf] = 0.0
        x_nan = np.isnan(x)
        x[x_nan] = 0.0
        x = np.absolute(x)
        dict[each] = x
    return dict

# 3rd feature extraction technique.
# Returns KPCA extracted features.
# Among 10000 features, 200 componets are chosen for each song using kernel = 'rbf'
def getKPCAcomp(dict_read):
    A = np.arange(10000)
    for key in dict_read.keys():
        if key<=1000:
            [sample_rate,X] = dict_read.get(key)
            # if song doesnt have 10000 features, then add 0s at the end (this usually isnt the case)
            if (len(X)<10000):
                dif = 10000 - len(X)
                for i in range(dif):
                    X = np.hstack((X,0.0))
            A = np.vstack((A,X[:10000]))
        else:
            break
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    kpca = KernelPCA(n_components=100, kernel="rbf")
    kpca.fit(A)
    A = kpca.transform(A)
    return A

# 3rd feature extraction technique tested.
# SelectKBest from scikit-learn is used
# From first 50000 features for each song, best 100 features are extracted
# chi-squared criterion is used for choosing K-Best features
def getKBest():
    A = np.arange(50000)
    for key in dict_read.keys():
        if key<=1000:
            [sample_rate,X] = dict_read.get(key)
            # if song doesnt have 50000 features, then add 0s at the end (this usually isnt the case)
            if (len(X)<50000):
                dif = 50000 - len(X)
                B = np.zeros(dif)
                X = np.hstack(X,B)
            A = np.vstack((A,X[:50000]))
        else:
            break
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    A = np.absolute(A)
    A = SelectKBest(chi2, k=99).fit_transform(A, y,)
    return A

# 3rd feature extraction technique tested.
# Discrete wavelet transform technique used and first 1000 features are extracted
# Returns dictionary of keys = [1,2,3,...900], values = first 100 DWT features for each song
def getWavelet():
    dict = {}
    for each in dict_read.keys():
        [sample_rate,X] = dict_read.get(each)
        cA, cD = pywt.dwt(X, 'db1')
        dict[each] = cA[:500]
    return dict

# MFCC averaged features dictionary is converted to numpy array, which is assigned to 'X' later for training
def mfccDictToNpArray(dict):
    A = np.arange(13)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    return A

# FFT features dictionary is converted to numpy array, which is assigned to 'X' later for training
def fftDictToNpArray(dict):
    A = np.arange(1000)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    return A

# Discrete Wavelet features dictionary is converted to numpy array, which is assigned to 'X' later for training
def wavletDictToNpArray(dict):
    X = dict.get(1)
    dim = len(X)
    print 'dim: '+str(dim)
    A = np.arange(dim)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    return A

# Helper function to pickle the file. Used only while debugging, not used in main code
def toPickle(a,file_name):
    with open(file_name, 'wb') as handle:
        pickle.dump(a, handle)

# Helper function to unpickle the file. Used only while debugging, not used in main code
def fromPickle(file_name):
    with open(file_name, 'rb') as handle:
        b = pickle.load(handle)
    return b

# defining the class labels, which is later assigned to 'y' while training classifiers
# Returns numpy array containing labels of songs --> [1,1,...,1,1,2,2,...,2,2,3,3,...,3,3,...........10,10,...,10,10]
def classArray():
    A = np.array([])
    for x in range(1,11):
        B = np.array([x])
        for y in range(90):
            A = np.hstack((A,B))
    A = A.astype(int)
    return A

# returns accuracy based on prediction (predict) and actual (y_test) numpy arrays
def getAccuracy(predict,y_test):
    count = 0.0
    for i in range(len(y_test)):
        if (y_test[i]==predict[i]):
            count += 1.0
    return (count/len(y_test))*100

# Function which takes previous existing confusion matrix, adds to it and returns it
def buildConfusionMatrix(conf,y_pre,y_act):
    for i in range (len(y_pre)):
        conf[y_pre[i]-1][y_act[i]-1] += 1
    return conf

# To print confusion matrix
def printConfusionMatrix(conf):
    print('\n'.join([''.join(['{:7}'.format(item) for item in row])
      for row in conf]))

# Calculating accuracy from Confusion Matrix after it is completely built from 10 folds
# Used to check if this value is same as obtained from getAccuracy() function.
def getAccuracyFromConfusion(conf):
    acc = 0.0
    total=0.0
    for i in range(len(conf[0])):
        for j in range(len(conf[0])):
            if i==j:
                acc += conf[i][j]
            total += conf[i][j]
    return format((acc/total)*100,'0.2f')

# Training a Gaussian Naive Bayes classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns: 1)averaged accuracy over 10 folds and 2) accuracy from confusion matrix ( just for checking if 1==2 )
def acc_naiveBayes():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        clf = GaussianNB()
        clf.fit(X_train, y_train)
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat)

# Training a Logistic Regression classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns: 1)averaged accuracy over 10 folds and 2) accuracy from confusion matrix ( just for checking if 1==2 )
def acc_logisticRegression():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        clf = LogisticRegression()
        clf.fit(X_train, y_train)
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat)

# Training a Random Forest classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns:  1) averaged accuracy over 10 folds;
#           2) accuracy from confusion matrix ( just for checking if 1==2 );
#           3) validation set prediction based on MFCC averaged features --> "valid_mfcc"
#           4) validation set prediction based on FFT averaged features --> "valid_fft"
#           5) validation set prediction based on KPCA averaged features --> "valid_kpca"
def acc_randomForest():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    clf = RandomForestClassifier(n_estimators=20,max_features=None,class_weight="balanced_subsample")
    valid_mfcc=0.0; valid_fft=0.0; valid_kpca=0.0
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        clf = clf.fit(X_train, y_train)
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    # Checking if the data set is transformed into MFCC(13) or FFT(1000) or KPCA features(else)
    if (X.shape[1]==13):
        print 'In 13 features if'
        valid_mfcc = clf.predict(validation_set_mfcc)
    elif (X.shape[1]==1000):
        print 'In 1000 features elif'
        valid_fft = clf.predict(validation_set_fft)
    elif (X.shape[1]==100):
        print 'In KPCA features else'
        valid_kpca = clf.predict(validation_set_kpca)
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat),valid_mfcc, valid_fft, valid_kpca

# Training a Adaboost classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns: 1)averaged accuracy over 10 folds and 2) accuracy from confusion matrix ( just for checking if 1==2 )
def acc_adaBoost():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        clf = AdaBoostClassifier(n_estimators=100)
        clf = clf.fit(X_train, y_train)
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat)

# Training a SVM classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns: 1)averaged accuracy over 10 folds and 2) accuracy from confusion matrix ( just for checking if 1==2 )
def acc_SVM():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        clf = SVC(kernel='rbf', probability=False)
        clf = clf.fit(X_train, y_train)
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat)

# Training a Ensemble Voting classifier and getting accuracy for each fold, and overall accuracy.
# This function also builds the confusion matrix for each fold and prints it
# Returns:  1) averaged accuracy over 10 folds;
#           2) accuracy from confusion matrix ( just for checking if 1==2 );
#           3) validation set prediction based on MFCC averaged features --> "valid_mfcc"
#           4) validation set prediction based on FFT averaged features --> "valid_fft"
#           5) validation set prediction based on KPCA averaged features --> "valid_kpca"
def acc_VotingClassifier():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    conf_mat = [[0 for i in range(10)] for j in range(10)]
    clf1 = GaussianNB()
    clf2 = RandomForestClassifier(n_estimators=20,max_features=None,class_weight="balanced_subsample")
    clf3 = SVC(kernel='rbf', probability=False)
    clf4 = LogisticRegression()
    eclf = VotingClassifier(estimators=[('gnb', clf1), ('rf', clf2),  ('lr', clf4)], voting='hard', weights=[1,3,3])
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        eclf = eclf.fit(X_train, y_train)
        y_predict = eclf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        conf_mat = buildConfusionMatrix(conf_mat,y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    # Checking if the data set is transformed into MFCC(13) or FFT(1000) or KPCA features(else)
    if (X.shape[1]==13):
        print 'In 13 features if'
        valid_mfcc = eclf.predict(validation_set_mfcc)
    elif (X.shape[1]==1000):
        print 'In 1000 features elif'
        valid_fft = eclf.predict(validation_set_fft)
    elif (X.shape[1]==100):
        print 'In KPCA features else'
        valid_kpca = eclf.predict(validation_set_kpca)
    acc = (acc/10.0)
    printConfusionMatrix(conf_mat)
    return acc, getAccuracyFromConfusion(conf_mat),valid_mfcc, valid_fft, valid_kpca

# Function to train all the classifiers using MFCC avegared features and get each classifier's accuracy
def get_MFCC_accuracies():
    print '*************   Accuracies from MFCC features  **************'
    print('Building Gaussian Naive Bayes')
    accuracy, accuracy_conf = acc_naiveBayes()
    print '--------Accuracy from Bayes------: '+ str(accuracy)
    print '--------Accuracy from Bayes, Confusion Matrix----'+str(accuracy_conf)

    print('Building Random Forest')
    accuracy, accuracy_conf,validation_mfcc_rf,validation_fft_rf, validation_kpca_rf = acc_randomForest()
    print '--------Accuracy from Random Forest------: '+ str(accuracy)
    print '--------Accuracy from Random Forest, Confusion Matrix----'+str(accuracy_conf)

    print('Building AdaBoost')
    accuracy, accuracy_conf = acc_adaBoost()
    print '--------Accuracy from AdaBoost------: '+ str(accuracy)
    print '--------Accuracy from AdaBoost, Confusion Matrix----'+str(accuracy_conf)

    print('Building SVM')
    accuracy, accuracy_conf = acc_SVM()
    print '--------Accuracy from SVM------: '+ str(accuracy)
    print '--------Accuracy from SVM, Confusion Matrix----'+str(accuracy_conf)

    print('Building Logistic Regression')
    accuracy,accuracy_conf = acc_logisticRegression()
    print '--------Accuracy from Logistic Regression------: '+ str(accuracy)
    print '--------Accuracy from Logistic Regression, Confusion Matrix----'+str(accuracy_conf)

    print('Building Voting Classifier')
    accuracy,accuracy_conf,validation_mfcc_vc,validation_fft_vc, validation_kpca_vc = acc_VotingClassifier()
    print '--------Accuracy from Voting Classifier------: '+ str(accuracy)
    print '--------Accuracy from Voting Classifier, Confusion Matrix----'+str(accuracy_conf)

    return validation_mfcc_rf, validation_mfcc_vc

# Function to train all the classifiers using FFT features
def get_FFT_accuracies():
    print '*************   Accuracies from FFT features  **************'
    print('Building Gaussian Naive Bayes')
    accuracy, accuracy_conf = acc_naiveBayes()
    print '--------Accuracy from Bayes------: '+ str(accuracy)
    print '--------Accuracy from Bayes, Confusion Matrix----'+str(accuracy_conf)

    print('Building Random Forest')
    accuracy, accuracy_conf,validation_mfcc_rf,validation_fft_rf, validation_kpca_rf = acc_randomForest()
    print '--------Accuracy from Random Forest------: '+ str(accuracy)
    print '--------Accuracy from Random Forest, Confusion Matrix----'+str(accuracy_conf)

    print('Building SVM')
    accuracy, accuracy_conf = acc_SVM()
    print '--------Accuracy from SVM------: '+ str(accuracy)
    print '--------Accuracy from SVM, Confusion Matrix----'+str(accuracy_conf)

    print('Building Logistic Regression')
    accuracy,accuracy_conf = acc_logisticRegression()
    print '--------Accuracy from Logistic Regression------: '+ str(accuracy)
    print '--------Accuracy from Logistic Regression, Confusion Matrix----'+str(accuracy_conf)

    print('Building Voting Classifier')
    accuracy,accuracy_conf,validation_mfcc_vc,validation_fft_vc, validation_kpca_vc = acc_VotingClassifier()
    print '--------Accuracy from Voting Classifier------: '+ str(accuracy)
    print '--------Accuracy from Voting Classifier, Confusion Matrix----'+str(accuracy_conf)

# Function to train all the classifiers using KPCA features
def get_KPCA_accuracies():
    print '*************   Accuracies from KPCA features  **************'
    print('Building Gaussian Naive Bayes')
    accuracy, accuracy_conf = acc_naiveBayes()
    print '--------Accuracy from Bayes------: '+ str(accuracy)
    print '--------Accuracy from Bayes, Confusion Matrix----'+str(accuracy_conf)

    print('Building Random Forest')
    accuracy, accuracy_conf,validation_mfcc_rf,validation_fft_rf, validation_kpca_rf = acc_randomForest()
    print '--------Accuracy from Random Forest------: '+ str(accuracy)
    print '--------Accuracy from Random Forest, Confusion Matrix----'+str(accuracy_conf)

    print('Building Logistic Regression')
    accuracy,accuracy_conf = acc_logisticRegression()
    print '--------Accuracy from Logistic Regression------: '+ str(accuracy)
    print '--------Accuracy from Logistic Regression, Confusion Matrix----'+str(accuracy_conf)

    print('Building Voting Classifier')
    accuracy,accuracy_conf,validation_mfcc_vc,validation_fft_vc, validation_kpca_vc = acc_VotingClassifier()
    print '--------Accuracy from Voting Classifier------: '+ str(accuracy)
    print '--------Accuracy from Voting Classifier, Confusion Matrix----'+str(accuracy_conf)

# Function to train all the classifiers using KBest features
def get_KBest_accuracies():
    print '*************   Accuracies from KBest features  **************'
    print('Building Gaussian Naive Bayes')
    accuracy, accuracy_conf = acc_naiveBayes()
    print '--------Accuracy from Bayes------: '+ str(accuracy)
    print '--------Accuracy from Bayes, Confusion Matrix----'+str(accuracy_conf)

    print('Building Random Forest')
    accuracy, accuracy_conf,validation_mfcc_rf,validation_fft_rf, validation_kpca_rf = acc_randomForest()
    print '--------Accuracy from Random Forest------: '+ str(accuracy)
    print '--------Accuracy from Random Forest, Confusion Matrix----'+str(accuracy_conf)

    print('Building SVM')
    accuracy, accuracy_conf = acc_SVM()
    print '--------Accuracy from SVM------: '+ str(accuracy)
    print '--------Accuracy from SVM, Confusion Matrix----'+str(accuracy_conf)

    print('Building Logistic Regression')
    accuracy,accuracy_conf = acc_logisticRegression()
    print '--------Accuracy from Logistic Regression------: '+ str(accuracy)
    print '--------Accuracy from Logistic Regression, Confusion Matrix----'+str(accuracy_conf)

    print('Building Voting Classifier')
    accuracy,accuracy_conf,validation_mfcc_vc,validation_fft_vc, validation_kpca_vc = acc_VotingClassifier()
    print '--------Accuracy from Voting Classifier------: '+ str(accuracy)
    print '--------Accuracy from Voting Classifier, Confusion Matrix----'+str(accuracy_conf)

# Function to train all the classifiers using Discrete Wavelet features
def get_Wavelet_accuracies():
    print '*************   Accuracies from Wavelet features  **************'
    print('Building Gaussian Naive Bayes')
    accuracy, accuracy_conf = acc_naiveBayes()
    print '--------Accuracy from Bayes------: '+ str(accuracy)
    print '--------Accuracy from Bayes, Confusion Matrix----'+str(accuracy_conf)

    print('Building Random Forest')
    accuracy, accuracy_conf,validation_mfcc_rf,validation_fft_rf, validation_kpca_rf = acc_randomForest()
    print '--------Accuracy from Random Forest------: '+ str(accuracy)
    print '--------Accuracy from Random Forest, Confusion Matrix----'+str(accuracy_conf)

    print('Building SVM')
    accuracy, accuracy_conf = acc_SVM()
    print '--------Accuracy from SVM------: '+ str(accuracy)
    print '--------Accuracy from SVM, Confusion Matrix----'+str(accuracy_conf)

    print('Building Logistic Regression')
    accuracy,accuracy_conf = acc_logisticRegression()
    print '--------Accuracy from Logistic Regression------: '+ str(accuracy)
    print '--------Accuracy from Logistic Regression, Confusion Matrix----'+str(accuracy_conf)

    print('Building Voting Classifier')
    accuracy,accuracy_conf,validation_mfcc_vc,validation_fft_vc, validation_kpca_vc = acc_VotingClassifier()
    print '--------Accuracy from Voting Classifier------: '+ str(accuracy)
    print '--------Accuracy from Voting Classifier, Confusion Matrix----'+str(accuracy_conf)

# Calculates the validation set songs average MFCC features and returns the numpy array
def validate():
    print 'Converting validation set songs into MFCC averaged features'
    val_dict_read = {}
    i = 1
    path = "validation/"
    dirs = os.listdir(path)
    for file in dirs:
        sample_rate,X = wavfile.read(path+file)
        val_dict_read[i] = [sample_rate,X]
        i += 1
    val_dict_mfcc = mfccIFY(val_dict_read)
    val_dict_mfcc_avg = getAvgMfcc(val_dict_mfcc)
    val_np_mfcc = mfccDictToNpArray(val_dict_mfcc_avg)
    print 'Finished MFCC, Doing FFT'
    val_dict_fft = fftIFY(val_dict_read)
    val_np_fft =fftDictToNpArray(val_dict_fft)
    print 'Finished FFT, doing KPCA'
    val_np_kpca = getKPCAcomp(val_dict_read)
    x_inf = np.isinf(val_np_kpca)
    val_np_kpca[x_inf] = 0.0
    x_nan = np.isnan(val_np_kpca)
    val_np_kpca[x_nan] = 0.0
    print 'Finished KPCA'
    return val_np_mfcc, val_np_fft, val_np_kpca

# Writing validation set predictions to "validation_files_prediction" file
def print_validation_mfcc(val_rf,val_vc):
    path = "validation/"
    dirs = os.listdir(path)
    print '********* Predicting converted MFCC values using Random Forest **********'
    j = 0
    f = open('validation_mfcc_random_forest','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_rf[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_rf[j]])+'\n')
        j += 1
    f.close()
    j = 0
    f = open('validation_mfcc_voting_classifier','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_vc[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_vc[j]])+'\n')
        j += 1
    f.close()

def print_validation_fft(val_rf,val_vc):
    path = "validation/"
    dirs = os.listdir(path)
    print '********* Predicting converted FFT values using Random Forest **********'
    j = 0
    f = open('validation_fft_random_forest','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_rf[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_rf[j]])+'\n')
        j += 1
    f.close()
    j = 0
    f = open('validation_fft_voting_classifier','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_vc[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_vc[j]])+'\n')
        j += 1
    f.close()

def print_validation_kpca(val_rf,val_vc):
    path = "validation/"
    dirs = os.listdir(path)
    print '********* Predicting converted KPCA values using Random Forest **********'
    j = 0
    f = open('validation_kpca_random_forest','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_rf[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_rf[j]])+'\n')
        j += 1
    f.close()
    j = 0
    f = open('validation_kpca_voting_classifier','w')
    for file in dirs:
        print str(file[:-4])+" " +str(genres_dict[val_vc[j]])
        f.write(str(file[:-4])+" " +str(genres_dict[val_vc[j]])+'\n')
        j += 1
    f.close()


# Storing all the genres in list
genres = ['blues','classical','country','disco','hiphop','jazz','metal','pop','reggae','rock']
# Storing genres in dictionary, used for final conversion from classifier predicted values to genre names
genres_dict = {1:'blues',2:'classical',3:'country',4:'disco',5:'hiphop',6:'jazz',7:'metal',8:'pop',9:'reggae',10:'rock'}
# Storing data file names in "dict_files" dictionary
print 'Assigning Indices'
dict_files = assignIndices()
# Storing read .wav files into "dict_read" dictionary
print 'Reading Files'
dict_read = readFiles()
# Applying FFT for above read files and storing in "dict_fft" dictionary
print 'Applying FFT to Data'
dict_fft = fftIFY(dict_read)
# Applying MFCC transformation for read files and storing in "dict_mfcc" dictionary
print 'Applying MFC to Data'
dict_mfcc = mfccIFY(dict_read)
# Averaging the MFCC features
dict_mfcc_avg = getAvgMfcc(dict_mfcc)
# Class labels(1 to 10) for 900 songs are assigned to 'y'
y = classArray()
# Validation set average MFCC features are assigned to "validation_set"
print 'Extracting all features from Validation set'
validation_set_mfcc, validation_set_fft, validation_set_kpca = validate()
# Applying DWT transform and storing in "dict_wavelet" dictionary
dict_wavelet = getWavelet()

# Converting the MFCC averged dictionary file to numpy array and assigning to "X" (to be used in classifier training)
X = mfccDictToNpArray(dict_mfcc_avg)
# Priting all MFCC accuracies with different classifiers and returning validation set prediction from Random Forest
validation_mfcc_rf, validation_mfcc_vc = get_MFCC_accuracies()
# Writing validation set predictions from MFCC features to:
#        1)"validation_mfcc_random_forest" --> based on Random Forest estimate
#        2)"validation_mfcc_voting_classifier"  --> based on Voting Classifier estimate
print_validation_mfcc(validation_mfcc_rf, validation_mfcc_vc)

# Converting the FFT features dictionary file to numpy array and assigning to "X" (to be used in classifier training)
X = fftDictToNpArray(dict_fft)
# Priting all FFT accuracies with different classifiers
validation_fft_rf, validation_fft_vc = get_FFT_accuracies()
# Writing validation set predictions from MFCC features to:
#        1)"validation_mfcc_random_forest" --> based on Random Forest estimate
#        2)"validation_mfcc_voting_classifier"  --> based on Voting Classifier estimate
print_validation_fft(validation_fft_rf, validation_fft_vc)

# Assigning KPCA features to "X" (to be used in classifier training)
X = getKPCAcomp(dict_read)
# Priting all KPCA accuracies with different classifiers
validation_kpca_rf, validation_kpca_vc = get_KPCA_accuracies()
# Writing validation set predictions from MFCC features to:
#        1)"validation_mfcc_random_forest" --> based on Random Forest estimate
#        2)"validation_mfcc_voting_classifier"  --> based on Voting Classifier estimate
print_validation_kpca(validation_kpca_rf, validation_kpca_vc)

# Assigning KBest features to "X" (to be used in classifier training)
X = getKBest()
# Priting all KBest accuracies with different classifiers
get_KBest_accuracies()
# Converting Wavelet features dictionary file to numpy array and assigning to "X" (to be used in classifier training)
X = wavletDictToNpArray(dict_wavelet)
# Priting all Wavelet accuracies with different classifiers
get_Wavelet_accuracies()
