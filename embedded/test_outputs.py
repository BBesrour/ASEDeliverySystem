try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop
from threading import Timer
import time


led = "green"

def change_led():
    global led
    ase_out.turn_off()
    time.sleep(1)
    if led == "green":
        ase_out.light_green()
        led = "red"
    else:
        ase_out.light_red()
        led = "green"
    Timer(1, change_led).start()


if __name__ == '__main__':
    change_led()
    ase_mainloop()
