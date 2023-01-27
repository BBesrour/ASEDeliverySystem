import sys
import traceback

try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError as e:
    traceback.print_exc()
    print(
        "Warning (it's fine): Could not import hardware. Using mock software for development instead." \
        "There should be a window popping up.",
        file=sys.stderr
    )
    from ase_io.software import ase_in, ase_out, ase_mainloop
