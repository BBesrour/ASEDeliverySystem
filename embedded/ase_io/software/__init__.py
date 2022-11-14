
from turtle import *

from .software_in import ASESoftwareIn
from .software_out import ASESoftwareOut

title("ASE Raspi Simulation")
setup(200, 200)
bgcolor("black")
ht()

ase_in = ASESoftwareIn()
ase_out = ASESoftwareOut()
listen()
ase_mainloop = mainloop
