try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop
from threading import Timer


def log_light_sensor():
    print("Light sensor: {}".format(ase_in.is_dark()))
    Timer(1, log_light_sensor).start()


if __name__ == '__main__':
    ase_in.add_listener(lambda card_content: print(card_content))
    log_light_sensor()
    ase_mainloop()
