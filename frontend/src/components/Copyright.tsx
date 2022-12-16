import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import * as React from "react";

export default function Copyright() {
    return (
        <Typography variant="body2" color="text.secondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="https://gitlab.lrz.de/ase-22-23/team40/ase-project-2022-team-40">
                ASE Group 40
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}