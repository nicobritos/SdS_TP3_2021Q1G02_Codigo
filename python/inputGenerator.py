import os
import sys
import random
import math
from random import randrange
import glob
# FORMATO INPUT
#M(cant particles)
#radio  masa    x   y   vx    vy
# ...
#radio  masa    x   y   vx    vy

def parse_args():
    # Get the total number of args passed
    total = len(sys.argv)
    if total != 2:
        print("1 argument needed, percentage of particles (float)")
        quit()

    percentage = float(sys.argv[1]) / 100

    return percentage


def generate(file_path, percentage):
    input = open(file_path, "w")
    w = 40  #0.12  w= 0.12 / 0.0030
    h = 30 #0.09   h=0.09/0.0030
    radius = 0.0015
    count = math.ceil(w * h * percentage)
    input.write(str(count))
    input.write('\n')
    present = {}

    while len(present) < count:
        print(str(len(present)))
        present[str(randrange(w)) +"_"+ str(randrange(h))] = True
    print(present)

    for y in range(h):
        for x in range(w):
            if str(x)+"_"+str(y) in present:
                # radius
                input.write(str(radius))
                input.write('\t')
                # mass 1kg
                input.write('1')
                input.write('\t')
                # x
                input.write(str(round(radius + x*2*radius,4)))
                input.write('\t')
                # y
                input.write(str(round(radius + y*2*radius,4)))
                input.write('\t')
                # vx 
                vx = round(random.uniform(-0.01, 0.01),4)
                input.write(str(vx))
                input.write('\t')
                # vy
                vy = round(math.sqrt(0.01**2 - vx**2),4)
                input.write(str(vy))
                input.write('\t')

                input.write('\n')

    input.close()


def main():
    
    percentage= parse_args()
    file_path = 'input.txt'
    generate(file_path, percentage)
    

if __name__ == '__main__':
    main()

