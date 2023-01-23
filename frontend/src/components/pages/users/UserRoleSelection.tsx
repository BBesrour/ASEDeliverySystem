import FormControl from "@mui/material/FormControl";
import { UserRole } from "../../../model/User";
import React from "react";
import Select from "@mui/material/Select";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";

export default function UserRoleSelection({role, setRole}: {
    role: UserRole, setRole: (role: UserRole) => void
}) {
    return <FormControl fullWidth>
        <InputLabel id="user-role-select-label">Role</InputLabel>
        <Select
            labelId="user-role-select-label"
            id="user-role-select"
            value={role}
            label="Role"
            onChange={(event) => setRole(event.target.value as UserRole)}
        >
            <MenuItem value="ROLE_CUSTOMER">Customer</MenuItem>
            <MenuItem value="ROLE_DELIVERER">Deliverer</MenuItem>
            <MenuItem value="ROLE_DISPATCHER">Dispatcher</MenuItem>
        </Select>
    </FormControl>;
}