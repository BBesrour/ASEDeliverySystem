import * as React from "react";
import Button from "@mui/material/Button";

export default function SignOutButton() {
    return <Button variant="contained" onClick={() => {
        localStorage.removeItem("accessToken");
        window.location.reload();
    }}>Sign out</Button>
}