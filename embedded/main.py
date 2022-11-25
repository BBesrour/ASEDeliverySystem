import time
from threading import Timer

from api.delivery_service import DeliveryService

try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop
from ase_io.card_content import CardContent
from common.config import Config

config = Config("config.json")
delivery_service = DeliveryService(config)


def check_box_closed():
    while not ase_in.is_dark():
        # blink red
        ase_out.light_red()
        time.sleep(0.5)
        ase_out.turn_off()
        time.sleep(0.5)
    delivery_service.send_box_closed_event()


def directly_close_box(is_dark: bool, timer: Timer):
    if is_dark:
        delivery_service.send_box_closed_event()
        timer.cancel()


def on_got_token(token: CardContent):
    print(f"Got token: {token}")
    if delivery_service.authenticate(token):
        ase_out.light_green()
        # If the box is not closed properly within 10 seconds, blink red
        t = Timer(10, check_box_closed).start()
        ase_in.add_token_listener(directly_close_box, t)
    else:
        ase_out.light_red()


if __name__ == '__main__':
    ase_out.light_green()
    ase_in.add_token_listener(on_got_token)
    ase_mainloop()
