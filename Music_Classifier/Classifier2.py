import scipy
from scipy.io import wavfile
#from matplotlib.pyplot import specgram
from scikits.talkbox.features import mfcc
import pickle
import numpy as np
from sknn.mlp import Classifier, Layer
from sklearn.cross_validation import KFold
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import MinMaxScaler
from sklearn.naive_bayes import GaussianNB
from sklearn.linear_model import LogisticRegression

def assignIndices():
    dict = {}
    index = 1
    genres = ['blues','classical','country','disco','hiphop','jazz','metal','pop','reggae','rock']
    for each in genres:
        for i in range(0,90):
            dict[index] = "wav_files/"+each+".000"+(str(i).zfill(2))+".wav"
            index += 1
    return dict

def readFiles():
    dict = {}
    for i in range(1,901):
        #print dict_files.get(i)
        sample_rate,X = wavfile.read(dict_files.get(i))
        dict[i] = [sample_rate,X]
    return dict

def fftIFY():
    dict = {}
    for each in dict_read.keys():
        [sample_rate,X] = dict_read.get(each)
        fft_features = abs(scipy.fft(X)[:1000])
        dict[each] = fft_features
        print "finished fftIFY of: "+str(each)+ " with file name: "+ str(dict_read.get(each))
    return dict

def mfccIFY():
    dict = {}
    for each in dict_read_pick.keys():
        [sample_rate,X] = dict_read_pick.get(each)
        ceps, mspec, spec = mfcc(X)
        dict[each] = ceps
        print "finished mfccIFY of: "+str(each)+ " with file name: "+ str(dict_read_pick.get(each))
    return dict

def toPickle(a,file_name):
    with open(file_name, 'wb') as handle:
        pickle.dump(a, handle)

def fromPickle(file_name):
    with open(file_name, 'rb') as handle:
        b = pickle.load(handle)
    return b

def getAvgMfcc():
    dict = {}
    for each in dict_mfcc_pick.keys():
        ceps = dict_mfcc_pick.get(each)
        num_ceps = ceps.shape[0]
        x = np.mean(ceps[int(num_ceps*(1.0/10)):int(num_ceps*(9.0/10))],axis=0)
        dict[each] = x
        print "finished avgMfcc of: "+str(each)+ ". Value is: "+str(x)
    return dict

def classArray():
    A = np.array([])
    for x in range(1,11):
        B = np.array([x])
        for y in range(90):
            A = np.hstack((A,B))
    A = A.astype(int)
    return A

def mfccDictToNpArray(dict):
    A = np.arange(13)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    return A

def fftDictToNpArray(dict):
    A = np.arange(1000)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = A.astype(float)
    return A

def getAccuracy(predict,y_test):
    count = 0.0
    for i in range(len(y_test)):
        if (y_test[i]==predict[i]):
            count += 1.0
    return (count/len(y_test))*100

def acc_naiveBayes():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        #print('--calling GaussianNB-- for '+str(temp)+'th time')
        clf = GaussianNB()
        #print('---Now, training using fit method-- for '+str(temp)+'th time')
        clf.fit(X_train, y_train)
        #print('--Predicting and printing--- for '+str(temp)+'th time')
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    return acc

def acc_neuralNetwork():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        nn = Classifier(layers=[Layer("Sigmoid", units=100),Layer("Softmax")],learning_rate=0.01,n_iter=100)
        nn.fit(X_train, y_train)
        y_predict = nn.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    return acc

def acc_logisticRegression():
    kf = KFold(900, n_folds=10,shuffle=True)
    acc = 0.0
    temp = 1
    for train_index, test_index in kf:
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]
        #print('--calling GaussianNB-- for '+str(temp)+'th time')
        clf = LogisticRegression()
        #print('---Now, training using fit method-- for '+str(temp)+'th time')
        clf.fit(X_train, y_train)
        #print('--Predicting and printing--- for '+str(temp)+'th time')
        y_predict = clf.predict(X_test)
        acc_loop = getAccuracy(y_predict,y_test)
        print("*** Accuracy*** for "+str(temp)+"th time: "+str(acc_loop))
        acc += acc_loop
        temp +=1
    acc = (acc/10.0)
    return acc


def normalizeData(np_arr):
    np_arr_norm = np.arange(1000)
    i=0
    for song in np_arr:
        temp = np.array([])
        for value in song:
            value *=(1.0/max(song))
            temp = np.hstack((temp,np.array([value])))
        np_arr_norm = np.vstack((np_arr_norm,temp))
        if((i!=0 and i%50 == 0) or i==1):
            print("i= "+str(i)+"--printing np_arr_norm[i]: "+str(np_arr_norm[i]))
        i += 1
    np_arr_norm = np.delete(np_arr_norm, 0, 0)
    return np_arr_norm

# dict_files = assignIndices()
# dict_read = readFiles()
# toPickle(dict_read,'readFile.pickle')

#dict_read_pick = fromPickle('readFile.pickle')
#print 'finished from readFile.pickle'

# dict_fft = fftIFY()
# toPickle(dict_fft,'fftFile.pickle')

#dict_fft_pick = fromPickle('fftFile.pickle')
#print 'finished from fftFile.pickle'

#dict_mfcc = mfccIFY()
#toPickle(dict_mfcc,'mfccFile.pickle')

dict_mfcc_pick = fromPickle('mfccFile.pickle')
#print 'finished from mfccFile.pickle'

#dict_mfcc_avg = getAvgMfcc()
#print 'pickling mfccAvgFile'
#toPickle(dict_mfcc_avg,'mfccAvgFile.pickle')

#dict_mfcc_avg_pick = fromPickle('mfccAvgFile.pickle')
#print 'finished from mfccAvgFile.pickle'

X = mfccDictToNpArray(dict_mfcc_pick)
y = classArray()

accuracy = acc_naiveBayes()
print '--------Accuracy from Bayes------: '+ str(accuracy)

#print('---Normalizing data---')
#X = normalizeData(X)

#print 'pickling normFftFile'
#toPickle(X,'normFftFile.pickle')
#X = fromPickle('normFftFile.pickle')
#print 'X[0]: '+ str(X[0])
print('Building neural network')
accuracy = acc_neuralNetwork()
print '--------Accuracy from NN------: '+ str(accuracy)

print('Building Logistic Regression')
accuracy = acc_logisticRegression()
print '--------Accuracy from Logistic Regression------: '+ str(accuracy)