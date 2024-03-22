import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import ClockIcon from '@mui/icons-material/Clock';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';

const TimePickerWrapper = (props) => {
  const { value: initialValue, onChange, format = 'hh:mm:ss', ...otherProps } = props;
  const [time, setTime] = useState(initialValue || ''); // Store time with AM/PM

  const handleChange = (newTime) => {
    if (newTime !== null) {
      const formattedTime = newTime.toLocaleTimeString([], {
        hour: format.includes('h') ? '2-digit' : undefined,
        minute: '2-digit',
        second: format.includes('s') ? '2-digit' : undefined,
      });
      const convertedTime = convertTime(formattedTime); // Convert to 24-hour format
      setTime(formattedTime);
      onChange(convertedTime); // Emit only 24-hour format
    }
  };

  const convertTime = (originalTime) => {
    const parts = originalTime.split(':');
    const hours = parseInt(parts[0], 10);
    const minutes = parts[1];
    const ampm = parts[2].slice(-2).toLowerCase();

    if (ampm === 'pm' && hours !== 12) {
      return `${hours + 12}:${minutes}`;
    } else if (ampm === 'am' && hours === 12) {
      return `00:${minutes}`;
    } else {
      return originalTime; // No change needed
    }
  };

  const handleOpenPicker = () => {
    setOpen(true);
  };

  const handleClosePicker = () => {
    setOpen(false);
  };

  const formatParts = format.split(':');

  return (
    <TextField
      label="Time"
      value={time}
      onChange={(event) => setTime(event.target.value)}
      InputProps={{
        endAdornment: (
          <InputAdornment position="end">
            <IconButton onClick={handleOpenPicker}>
              <ClockIcon />
            </IconButton>
          </InputAdornment>
        ),
        inputProps: {
          pattern: formatParts.map((part) =>
            part === 'h' ? '[0-1][0-9]|2[0-3]' : '[0-5][0-9]'
          ).join(''),
        },
      }}
      {...otherProps}
    >
      <TimePicker
        open={open}
        value={time}
        onChange={handleChange}
        onClose={handleClosePicker}
        renderInput={(params) => <></>} // Hide the default TimePicker input
        components={{
          OpenPickerButton: () => null, // Remove default open button
        }}
      />
    </TextField>
  );
};

export default TimePickerWrapper;
