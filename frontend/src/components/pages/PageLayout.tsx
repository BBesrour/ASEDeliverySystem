import createTheme from "@mui/material/styles/createTheme";
import ThemeProvider from "@mui/material/styles/ThemeProvider";
import CssBaseline from "@mui/material/CssBaseline";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import CameraIcon from "@mui/icons-material/PhotoCamera";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Container from "@mui/material/Container";
import Footer from "../Footer";
import * as React from "react";
import Stack from "@mui/material/Stack";

const theme = createTheme();

export default function PageLayout({title, description, actionButtons, content}: {
    title: string | null,
    description: string | null,
    actionButtons: React.ReactNode,
    content: React.ReactNode
}) {
    return <ThemeProvider theme={theme}>
        <CssBaseline/>
        {title ?
            <AppBar position="relative">
                <Toolbar>
                    <CameraIcon sx={{mr: 2}}/>
                    <Typography variant="h6" color="inherit" noWrap>
                        {title}
                    </Typography>
                </Toolbar>
            </AppBar> : null}
        <main>
            {/* Hero unit */}
            <Box
                sx={{
                    bgcolor: 'background.paper',
                    pt: 8,
                    pb: 6,
                }}
            >
                <Container maxWidth="sm">
                    <Typography
                        component="h1"
                        variant="h2"
                        align="center"
                        color="text.primary"
                        gutterBottom
                    >
                        {title}
                    </Typography>
                    {description ?
                        <Typography variant="h5" align="center" color="text.secondary" paragraph>
                            {description}
                        </Typography> : null}
                    <Stack
                        sx={{pt: 4}}
                        direction="row"
                        spacing={2}
                        justifyContent="center"
                    >
                        {actionButtons}
                    </Stack>
                </Container>
            </Box>
            <Container sx={{py: 8}} maxWidth="md">
                {content}
            </Container>
        </main>
        <Footer/>
    </ThemeProvider>
}