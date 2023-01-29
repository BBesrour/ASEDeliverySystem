import React, { useEffect, useState } from "react";
import { Autocomplete } from "@mui/material";
import TextField from "@mui/material/TextField";
import User, { UserRole } from "../../../model/User";
import { getAllUsers } from "../../../api/delivery/user";

export default function UserSelection({
  label,
  onSelect,
  userRole,
  userId,
}: {
  label: string;
  onSelect: (user: User | null) => void;
  userRole: UserRole;
  userId: string;
}) {
  const [users, setUsers] = useState<User[]>([]);
  useEffect(() => {
    getAllUsers().then((users) => {
      setUsers(users);
    });
  }, []);

  function getUserByID(UserID: string): User | null {
    return users.find((user) => user.id === UserID) || null;
  }

  return (
    <Autocomplete
      disablePortal
      defaultValue={userId}
      options={users.filter((u) => u.role === userRole).map((u) => u.id)}
      sx={{ width: 300, marginBottom: 1 }}
      renderInput={(params) => <TextField {...params} label={label} />}
      getOptionLabel={(option) => getUserByID(option ?? "")?.email || ""}
      onChange={(event, newValue) => {
        onSelect(getUserByID(newValue ?? ""));
      }}
    />
  );
}
