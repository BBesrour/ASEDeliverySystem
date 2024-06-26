import * as React from 'react';
import {useState} from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Router from '../Router';
import PageLayout from "./PageLayout";
import {login} from "../../api/auth/signup";
import {getAccessToken, setAccessToken, setRole, setUserID} from "../../storage/user";

export default function SignInPage() {
    const [success, setSuccess] = useState(!!getAccessToken());
    const [loginError, setLoginError] = useState("");
    const handleSubmit = async (event: { preventDefault: () => void; currentTarget: HTMLFormElement | undefined; }) => {
        event.preventDefault();
        setLoginError("");
        const data = new FormData(event.currentTarget);
        const email: string = data.get("email") as string;
        const password: string = data.get("password") as string;
        try {
            const response = await login(email, password);
            setAccessToken(response.accessToken);
            setRole(response.role);
            setUserID(response.id);
        } catch (e) {
            setLoginError("Wrong login. Please try again.");
            return;
        }
        setSuccess(!!getAccessToken());
        if (!getAccessToken()) {
            setLoginError("Login failed. Please try again.");
        }
    };

    return (
        <>
            {success ? (
                <main className="App">
                    <Router/>
                </main>
            ) : (
                <PageLayout
                    title={null}
                    description={null}
                    actionButtons={<></>}
                    content={
                        <><CssBaseline/><Box
                            sx={{
                                marginTop: 8,
                                display: 'flex',
                                flexDirection: 'column',
                                alignItems: 'center',
                            }}
                        >
                            <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                                <LockOutlinedIcon/>
                            </Avatar>
                            <Typography component="h1" variant="h5">
                                Sign in
                            </Typography>
                            <Box component="form" onSubmit={handleSubmit} noValidate sx={{mt: 1}}>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    id="email"
                                    label="Email Address"
                                    name="email"
                                    autoComplete="email"
                                    autoFocus/>
                                <TextField
                                    margin="normal"
                                    required
                                    fullWidth
                                    name="password"
                                    label="Password"
                                    type="password"
                                    id="password"
                                    autoComplete="current-password"/>
                                <p>{loginError}</p>
                                <Button
                                    type="submit"
                                    fullWidth
                                    variant="contained"
                                    sx={{mt: 3, mb: 2}}
                                >
                                    Sign In
                                </Button>
                            </Box>
                        </Box></>
                    }
                />
            )}
        </>
    );
}
