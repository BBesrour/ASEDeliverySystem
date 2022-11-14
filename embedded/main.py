try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop


if __name__ == '__main__':
    ase_out.light_green()
    ase_in.add_listener(lambda card_content: print(card_content))
    ase_mainloop()
