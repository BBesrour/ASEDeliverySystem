import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Copyright from "./Copyright";
import * as React from "react";
import SignOutButton from "./SignOutButton";

export default function Footer() {
    return <Box sx={{ bgcolor: 'background.paper', p: 6 }} component="footer">
        <Typography variant="h6" align="center" gutterBottom>
            ASE 2022/2023
        </Typography>
        <SignOutButton />
        <Copyright />
    </Box>
}