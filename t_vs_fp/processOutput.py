import os
import sys
import csv
import os.path
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches

# Get the total number of args passed
total = len(sys.argv)
if total != 1:
    quit()
indexCsv1 = 0
indexCsv2 = 0
indexCsv3 = 0
csvfile1 = "ap=0.01/output_"+ str(indexCsv1)+".csv"
csvfile2 = "ap=0.02/output_"+ str(indexCsv2)+".csv"
csvfile3 = "ap=0.04/output_"+ str(indexCsv2)+".csv"

csvfile = "1ap=0.01/output_"+ str(indexCsv1)+".csv"
ap1_001y = []
ap1_001x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_001y.append(float(line[9]))
        ap1_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.01/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.01/output_"+ str(indexCsv1)+".csv"

ap2_001y = []
ap2_001x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_001y.append(float(line[9]))
        ap2_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.01/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.01/output_"+ str(indexCsv1)+".csv"

ap3_001x = []
ap3_001y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_001y.append(float(line[9]))
        ap3_001x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.01/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_001x), len(ap2_001x), len(ap3_001x))
ap_001x = []
ap_001y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_001x):
        mean += ap1_001y[i]
        j+=1
    if i < len(ap2_001x):
        mean += ap2_001y[i]
        j+=1
    if i < len(ap3_001x):
        mean += ap3_001y[i]
        j+=1
    mean /=j
    ap_001x.append(ap1_001x[i])
    ap_001y.append(mean)






csvfile = "1ap=0.02/output_"+ str(indexCsv1)+".csv"
ap1_002y = []
ap1_002x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_002y.append(float(line[9]))
        ap1_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.02/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.02/output_"+ str(indexCsv1)+".csv"

ap2_002y = []
ap2_002x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_002y.append(float(line[9]))
        ap2_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.02/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.02/output_"+ str(indexCsv1)+".csv"

ap3_002x = []
ap3_002y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_002y.append(float(line[9]))
        ap3_002x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.02/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_002x), len(ap2_002x), len(ap3_002x))
ap_002x = []
ap_002y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_002x):
        mean += ap1_002y[i]
        j+=1
    if i < len(ap2_002x):
        mean += ap2_002y[i]
        j+=1
    if i < len(ap3_002x):
        mean += ap3_002y[i]
        j+=1
    mean /=j
    ap_002x.append(ap3_002x[i])
    ap_002y.append(mean)






csvfile = "1ap=0.04/output_"+ str(indexCsv1)+".csv"
ap1_004y = []
ap1_004x = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap1_004y.append(float(line[9]))
        ap1_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "1ap=0.04/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "2ap=0.04/output_"+ str(indexCsv1)+".csv"

ap2_004y = []
ap2_004x = []
t = 0.0
dt = 0.1
if os.path.isfile(csvfile):
    print("exists")

while os.path.isfile(csvfile):
    print("2")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1)> t+dt:
        t +=dt
        t = round(t,1)
        ap2_004y.append(float(line[9]))
        ap2_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "2ap=0.04/output_"+ str(indexCsv1)+".csv"

indexCsv1 =0
csvfile = "3ap=0.04/output_"+ str(indexCsv1)+".csv"

ap3_004x = []
ap3_004y = []
t = 0.0
dt = 0.1
while os.path.isfile(csvfile):
    print("3")
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 1) > t+dt:
        t +=dt
        t = round(t,1)
        ap3_004y.append(float(line[9]))
        ap3_004x.append(float(line[8]))
    indexCsv1 +=1
    csvfile = "3ap=0.04/output_"+ str(indexCsv1)+".csv"

ran = max(len(ap1_004x), len(ap2_004x), len(ap3_004x))
ap_004x = []
ap_004y = []
for i in range(ran):
    mean = 0
    j=0
    if i < len(ap1_004x):
        mean += ap1_004y[i]
        j+=1
    if i < len(ap2_004x):
        mean += ap2_004y[i]
        j+=1
    if i < len(ap3_004x):
        mean += ap3_004y[i]
        j+=1
    mean /=j
    ap_004x.append(ap3_004x[i])
    ap_004y.append(mean)







plt.ylim((0, 1.5))

# plt.plot(ap1_001x, ap1_001y,4)
# plt.plot(ap2_001x, ap2_001y,4)
# plt.plot(ap3_001x,ap3_001y,4)
plt.plot(ap_001x,ap_001y,4)
plt.plot(ap_002x,ap_002y,4)
plt.plot(ap_004x,ap_004y,4)


blue_patch = mpatches.Patch(color='blue', label='apertura=0.01')
green_patch = mpatches.Patch(color='green', label='apertura=0.02')
purple_patch = mpatches.Patch(color='purple', label='apertura=0.04')
plt.legend(handles=[green_patch, blue_patch, purple_patch])

plt.xlabel('Tiempo (s)')
plt.ylabel('Fraccion de particulas del lado izquierdo')
plt.title('Fraccion de particulas vs Tiempo')
plt.savefig('fp_t.png')
plt.clf()






# fp = 1.1

# f1x = []
# f1y = []
# x= 0.0
# y= 1.0
# while os.path.isfile(csvfile1):
#     f = open(csvfile1) 
#     f.readline()
#     line = f.readline().split(';')
#     if round(float(line[9]), 3) == fp:
#         x =  float(line[8])
#         y = float(line[9])
#     else:
#         f1y.append(y)
#         f1x.append(x)
#         fp = round(float(line[9]), 3)
#         f1y.append(float(line[9]))
#         f1x.append(float(line[8]))
#     indexCsv1 +=1
#     csvfile1 = "ap=0.01/ap=0.01/output_"+ str(indexCsv1)+".csv"


# fp = 1.1
# x= 0.0
# y= 1.0
# f2x = []
# f2y = []
# time = 0
# while os.path.isfile(csvfile2):
#     f = open(csvfile2) 
#     f.readline()
#     line = f.readline().split(';')
#     if round(float(line[9]), 3) == fp:
#         x =  float(line[8])
#         y = float(line[9])
#         if float(line[8]) <time:
#             print(str(time))
#             print("forom up to down")
#             print(line[8])
#             print(indexCsv2)
#         time = float(line[8])
#     else:
#         if float(line[8]) <time:
#             print(str(time))
#             print("forom up to down")
#             print(line[8])
#             print(indexCsv2)
#         time = float(line[8])
#         f2y.append(y)
#         f2x.append(x)
#         fp = round(float(line[9]), 3)
#         f2y.append(float(line[9]))
#         f2x.append(float(line[8]))
#     indexCsv2 +=1
#     csvfile2 = "ap=0.05/output copia/output_"+ str(indexCsv2)+".csv"

# print("f3")
# fp = 1.1
# x= 0.0
# y= 1.0
# f3x = []
# f3y = []

# while os.path.isfile(csvfile3):
#     f = open(csvfile3) 
#     f.readline()
#     line = f.readline().split(';')
#     if round(float(line[9]), 3) == fp:
#         x =  float(line[8])
#         y = float(line[9])
#     else:
#         f3y.append(y)
#         f3x.append(x)
#         fp = round(float(line[9]), 3)
#         f3y.append(float(line[9]))
#         f3x.append(float(line[8]))
#     indexCsv3 +=1
#     csvfile3 = "ap=0.005/output copia/output_"+ str(indexCsv3)+".csv"


# plt.ylim((0, 1.5))

# plt.plot(f1x, f1y,4)
# plt.plot(f2x, f2y,4)
# plt.plot(f3x, f3y,4)


# blue_patch = mpatches.Patch(color='blue', label='ap=0.01')
# green_patch = mpatches.Patch(color='green', label='ap=0.05')
# purple_patch = mpatches.Patch(color='purple', label='ap=0.005')
# plt.legend(handles=[green_patch, blue_patch, purple_patch])

# plt.xlabel('Tiempo (s)')
# plt.ylabel('Fraccion de particulas del lado izquierdo')
# plt.title('Fraccion de particulas vs Tiempo')
# plt.savefig('fp_t.png')
# plt.clf()
     
    

