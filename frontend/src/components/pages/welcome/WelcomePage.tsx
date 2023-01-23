import React from "react";
import SignOutButton from "../../SignOutButton";
import Typography from "@mui/material/Typography";

export default function WelcomePage() {
    return (
        <div>
            <Typography variant="h2" component="h1" gutterBottom>
                Welcome to the delivery management!
            </Typography>
            <Typography>
                Here you can manage deliveries, boxes and users (depending on your role).
            </Typography>
            <br />

            <SignOutButton />
        </div>
    );
}