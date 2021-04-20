import os
import sys
import csv
import os.path

def print_row(row, file_path):
    ovito = open(file_path, "a")
    
    ovito.write(row[0] +'\t'+row[1]+'\t'+row[2]+'\t'+row[3]+'\t'+row[4]+'\t'+row[5]+'\t'+row[6]+'\n')

    ovito.close()

def get_num_particles_in_walls():
    w = 80  #0.24  w= 0.24 / 0.0030
    h = 30 #0.09   h=0.09/0.0030
    hmw =int( ((0.09/2) - (aperture/2) )/0.003)
    return w*2 + h*2 + hmw *2

def print_walls(file_path, ids, aperture):
    w = 80  #0.24  w= 0.24 / 0.0030
    h = 30 #0.09   h=0.09/0.0030
    hmw =int( ((0.09/2) - (aperture/2) )/0.003)
    radius=0.0015
    mass =1.0
    ovito = open(file_path, "a")
    xp=0
    for x in range(w):
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(xp)+'\t'+str(0)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(xp)+'\t'+str(0.09)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        xp +=(radius *2)
    yp=0
    for y in range(h):
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(0)+'\t'+str(yp)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(0.24)+'\t'+str(yp)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        yp+=radius* 2
    yp = 0
    for y in range(hmw):
        x = 0.12
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(x)+'\t'+str(yp)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        ids += 1
        ovito.write(str(ids) +'\t'+str(radius)+'\t'+str(x)+'\t'+str(0.09 - yp)+'\t'+str(mass)+'\t'+'0'+'\t'+'0'+'\t'+'1'+'\n')
        yp+=radius* 2
    ovito.close()



# Get the total number of args passed
total = len(sys.argv)
if total != 4:
    print("3 arguments needed, 1. folder path of the output files, 2. apertura en la pared intermedia, 3. delta time in s")
    quit()
aperture = float(sys.argv[2])
indexCsv = 0
csvfile = str(sys.argv[1]) +"/output_"+ str(indexCsv)+".csv"
ovito_index = 0
ovito_file = "ovito." + str(ovito_index) +".txt"

while os.path.isfile(ovito_file):
    os.remove(ovito_file)
    ovito_index +=1
    ovito_file = "ovito." + str(ovito_index) +".txt"

ovito_index = 0
ovito_file = "ovito." + str(ovito_index) +".txt"


t = 0.0
dt = float(sys.argv[3])
while os.path.isfile(csvfile):
    f = open(csvfile) 
    f.readline()
    line = f.readline().split(';')
    if round(float(line[8]), 3) < t:
        indexCsv +=1
        csvfile = str(sys.argv[1]) +"/output_"+ str(indexCsv)+".csv"
        continue
    elif round(float(line[8]), 3) > t+dt:
        print("dt is too small")
        print("it should be at least of " + str(round(float(line[8]), 3) - t))
        exit()
    numline = len(f.readlines())

    with open(csvfile) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=';')
        line_count = 0
        for row in csv_reader:
            if line_count == 0:
                ovito = open(ovito_file, "a")
                ovito.write(str(numline+get_num_particles_in_walls()))
                ovito.write('\n')
                ovito.close()
            else:
                print_row(row, ovito_file)
            line_count +=1
        print_walls(ovito_file,numline, aperture)
    t +=dt
    ovito_index +=1
    ovito_file = "ovito." + str(ovito_index) +".txt"
    indexCsv +=1
    csvfile = str(sys.argv[1]) +"/output_"+ str(indexCsv)+".csv"

if ovito_index <10:
    print("perhaps dt is too big, try something smaller")

   
            
    

