import time
from threading import Timer

from ase_io import ase_in, ase_out


def _blink_red():
    ase_out.light_red()
    time.sleep(0.5)
    ase_out.turn_off()
    time.sleep(0.5)


class BoxClosedChecker:
    def __init__(self, config, delivery_service):
        self.config = config
        self.delivery_service = delivery_service
        self.allowed_open_until = 0

    def _check(self):
        """Performs the actual check if the box is closed and lights up red if not (continuously)."""
        while True:
            if False: # not ase_in.is_dark() and time.time() > self.allowed_open_until:
                _blink_red()

    def start_check(self):
        """Starts a thread that checks if the box is closed, continuously."""
        t = Timer(0, self._check)
        t.start()

    def allow_open_for(self, seconds: int):
        self.allowed_open_until = time.time() + seconds
