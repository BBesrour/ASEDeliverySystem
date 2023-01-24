import React, {useEffect, useState} from "react";
import {Autocomplete} from "@mui/material";
import TextField from "@mui/material/TextField";
import User, {UserRole} from "../../../model/User";
import {getAllUsers} from "../../../api/delivery/user";

interface AutocompleteDelivererOption {
    delivererID: string;
    label: string;
}

export default function UserSelection({label, onSelect, userRole}: { label: string, onSelect: (user: User | null) => void, userRole: UserRole }) {
    const [users, setUsers] = useState<User[]>([]);
    useEffect(() => {
        getAllUsers().then((users) => {
            setUsers(users);
        });
    }, []);

    const [userOptions, setUserOptions] = useState<AutocompleteDelivererOption[]>([]);
    useEffect(() => {
        setUserOptions(users.filter((user) => user.role === userRole).map((user) => {
            return {
                delivererID: user.id || "",
                label: user.name
            }
        }));
    }, [users]);

    function getUserByID(UserID: string): User | null {
        return users.find((user) => user.id === UserID) || null;
    }

    return <Autocomplete
        disablePortal
        options={userOptions}
        getOptionLabel={(user) => user.delivererID}
        sx={{width: 300}}
        renderInput={(params) => <TextField {...params} label={label}/>}
        onChange={(event, newValue) => {
            onSelect(getUserByID(newValue?.delivererID || ""));
        }}
    />;
}
