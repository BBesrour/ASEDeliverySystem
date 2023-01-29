import React, { useEffect, useState } from "react";
import { Autocomplete } from "@mui/material";
import TextField from "@mui/material/TextField";
import User, { UserRole } from "../../../model/User";
import { getAllUsers } from "../../../api/delivery/user";

interface AutocompleteDelivererOption {
  userID: string;
  label: string;
}

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

  const [userOptions, setUserOptions] = useState<AutocompleteDelivererOption[]>(
    []
  );
  useEffect(() => {
    setUserOptions(
      users
        .filter((user) => user.role === userRole)
        .map((user) => {
          return {
            userID: user.id || "",
            label: user.email,
          };
        })
    );
  }, [users]);

  function getUserByID(UserID: string): User | null {
    return users.find((user) => user.id === UserID) || null;
  }

  console.log(getUserByID(userId)?.email);
  return (
    <Autocomplete
      disablePortal
      options={userOptions}
      isOptionEqualToValue={(option, value) => option.userID === value.userID}
      defaultValue={{ userID: userId, label: getUserByID(userId)?.email || "" }}
      getOptionLabel={(user) => {
        console.log(user);
        return user.label;
      }}
      sx={{ width: 300 }}
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          defaultValue={getUserByID(userId)?.email || ""}
        />
      )}
      onChange={(event, newValue) => {
        onSelect(getUserByID(newValue?.userID || ""));
      }}
    />
  );
}
