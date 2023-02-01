import Box from "../../../model/Box";
import { getBoxes } from "../../../api/delivery/box";
import React, { useEffect, useState } from "react";
import Autocomplete from "@mui/material/Autocomplete";
import TextField from "@mui/material/TextField";

interface AutocompleteBoxOption {
  boxID: string;
  label: string;
}

export default function BoxSelection({
  label,
  onSelect,
  boxID,
}: {
  label: string;
  onSelect: (box: Box | null) => void;
  boxID: string;
}) {
  const [boxes, setBoxes] = useState<Box[]>([]);
  useEffect(() => {
    getBoxes().then((boxes) => {
      setBoxes(boxes);
    });
  }, []);

  const [boxOptions, setBoxOptions] = useState<AutocompleteBoxOption[]>([]);
  useEffect(() => {
    setBoxOptions(
      boxes.map((box) => {
        return {
          boxID: box.id || "",
          label: box.name,
        };
      })
    );
  }, [boxes]);

  function getBoxByID(boxID: string): Box | null {
    return boxes.find((box) => box.id === boxID) || null;
  }

  return (
    <Autocomplete
      disablePortal
      sx={{ width: 300, marginBottom: 1 }}
      options={boxOptions}
      isOptionEqualToValue={(option, value) => option.boxID === value.boxID}
      renderOption={(props, option) => {
        return (
          <li {...props} key={option.boxID}>
            {option.label}
          </li>
        );
      }}
      renderInput={(params) => <TextField {...params} label={label} />}
      onChange={(event, newValue) => {
        onSelect(getBoxByID(newValue?.boxID || ""));
      }}
    />
  );
}
