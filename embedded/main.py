from api.delivery_service import DeliveryService
try:
    from ase_io.hardware import ase_in, ase_out, ase_mainloop
except ImportError:
    from ase_io.software import ase_in, ase_out, ase_mainloop
from common.config import Config

config = Config("config.json")
delivery_service = DeliveryService(config)


if __name__ == '__main__':
    ase_out.light_green()
    ase_in.add_listener(lambda card_content: print(card_content))
    ase_mainloop()
