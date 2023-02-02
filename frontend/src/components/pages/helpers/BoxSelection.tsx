import Box from "../../../model/Box";
import { getBoxes } from "../../../api/delivery/box";
import React, { useEffect, useState } from "react";
import Autocomplete from "@mui/material/Autocomplete";
import TextField from "@mui/material/TextField";

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

  function getBoxByID(boxID: string): Box | null {
    return boxes.find((box) => box.id === boxID) || null;
  }

  return (
    <Autocomplete
      disablePortal
      sx={{ width: 300, marginBottom: 1 }}
      defaultValue={boxID}
      options={boxes.map((b) => b.id)}
      getOptionLabel={(option) => getBoxByID(option ?? "")?.name || ""}
      renderInput={(params) => <TextField {...params} label={label} />}
      onChange={(event, newValue) => {
        onSelect(getBoxByID(newValue ?? ""));
      }}
    />
  );
}
