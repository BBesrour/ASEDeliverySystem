from api.delivery_service import DeliveryService
from ase_io import ase_in, ase_out, ase_mainloop
from ase_io.card_content import CardContent
from common.config import Config
from logic.box_closed_checker import BoxClosedChecker

config = Config("config.json")
delivery_service = DeliveryService(config)
box_closed_checker = BoxClosedChecker(config, delivery_service)


def on_next_box_close(callback):
    """Calls the callback once when the box is closed (is_dark False => True change)."""
    last_darkness_state = ase_in.is_dark()

    def on_darkness_changed(is_dark: bool):
        nonlocal last_darkness_state
        if not last_darkness_state and is_dark:
            ase_in.remove_darkness_listener(on_darkness_changed)
            callback()
        last_darkness_state = is_dark

    ase_in.add_darkness_listener(on_darkness_changed)


def on_got_token(token: CardContent):
    print(f"Got token: {token}")
    if delivery_service.authenticate(token):
        ase_out.light_green()
        box_closed_checker.allow_open_for(seconds=10)
        on_next_box_close(lambda: delivery_service.send_box_closed_event(token.token))
    else:
        ase_out.light_red()


if __name__ == '__main__':
    import time

    ase_out.light_green()
    time.sleep(1)
    ase_out.turn_off()
    box_closed_checker.start_check()
    ase_in.add_token_listener(on_got_token)
    ase_mainloop()
