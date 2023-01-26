import requests

from api.user import User
from ase_io.card_content import CardContent
from common.config import Config


class DeliveryService:
    def __init__(self, config: Config):
        self.config = config

    def _get(self, url: str, params=None):
        return requests.get(
            self.config.delivery_server_address + url,
            params=params,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _post(self, url: str, data=None):
        return requests.post(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def _put(self, url: str, data=None):
        return requests.put(
            self.config.delivery_server_address + url,
            data=data,
            headers={"Authorization": f"Bearer {self.config.delivery_server_access_token}"}
        )

    def get_user(self, card_token: str) -> User | None:
        """Return the user with the given card token."""
        user_response = self._get(f"/user/token-to-user", {"token": card_token})
        if user_response.text:
            user_json = user_response.json()
            return User(user_json["id"], user_json["name"], user_json["email"])
        return None

    def authenticate(self, card_content: CardContent):
        """Return True whether the card token is valid."""
        card_token = card_content.token
        user = self.get_user(card_token)
        return user is not None

    def send_box_closed_event(self):
        """Send a box closed event to the delivery server."""
        self._post(f"/box/{self.config.box_id}/closed")
