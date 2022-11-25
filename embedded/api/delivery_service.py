import requests

from common.config import Config
from ase_io.card_content import CardContent


def _is_deliverer_token(card_token: str) -> bool:
    """Return whether the card token is a deliverer token."""
    return card_token.startswith("D")


def _is_customer_token(card_token: str) -> bool:
    """Return whether the card token is a customer token."""
    return card_token.startswith("C")


class DeliveryService:
    def __init__(self, config: Config):
        self.config = config

    def _get(self, url: str, data=None):
        return requests.get(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _post(self, url: str, data=None):
        return requests.post(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _authenticate_deliverer(self, card_token: str) -> bool:
        """Return whether the deliverer card token is valid."""
        resp = self._get(
            f"/deliverer/authenticate/{self.config.box_id}",
            data={"card_token": card_token}
        )
        return resp.status_code == 200

    def _authenticate_customer(self, card_token: str) -> bool:
        """Return whether the customer card token is valid."""
        resp = self._get(
            f"/customer/authenticate/{self.config.box_id}",
            data={"card_token": card_token}
        )
        return resp.status_code == 200

    def authenticate(self, card_content: CardContent):
        """Return True whether the card token is valid."""
        card_token = card_content.token
        if _is_deliverer_token(card_token):
            return self._authenticate_deliverer(card_token)
        elif _is_customer_token(card_token):
            return self._authenticate_customer(card_token)
        return False

    def send_box_closed_event(self):
        """Send a box closed event to the delivery server."""
        self._post(f"/box/{self.config.box_id}/closed")
