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

def dictToNpArray(dict):
    A = np.arange(13)
    for key in dict.keys():
        A = np.vstack((A,dict.get(key)))
    A = np.delete(A, 0, 0)
    A = np.float128(A)
    return A

def getAccuracy():
    count = 0.0
    for i in range(len(y_test)):
        if (y_test[i]==y_test_nn[i]):
            count += 1.0
    return (count/len(y_test))*100

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

#dict_mfcc_pick = fromPickle('mfccFile.pickle')
#print 'finished from mfccFile.pickle'

#dict_mfcc_avg = getAvgMfcc()
#print 'pickling mfccAvgFile'
#toPickle(dict_mfcc_avg,'mfccAvgFile.pickle')
dict_mfcc_avg_pick = fromPickle('mfccAvgFile.pickle')
print 'finished from mfccAvgFile.pickle'

X = dictToNpArray(dict_mfcc_avg_pick)
y = classArray()
kf = KFold(900, n_folds=10,shuffle=True)
print(kf)
for train_index, test_index in kf:
    X_train, X_test = X[train_index], X[test_index]
    y_train, y_test = y[train_index], y[test_index]

# nn = Classifier(
#     layers=[
#         Layer("Rectifier", units=50),
#         Layer("Linear")],
#     learning_rate=0.02,
#     n_iter=20)
# nn.fit(X_train, y_train)
pipeline = Pipeline([
        ('min/max scaler', MinMaxScaler(feature_range=(0.0, 1.0))),
        ('neural network', Classifier(layers=[Layer("Softmax")], n_iter=25))])
pipeline.fit(X_train, y_train)

y_test_nn = pipeline.predict(X_test)

acc = getAccuracy()
print acc