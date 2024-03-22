import React, { useState } from 'react';
import TextField from '@mui/material/TextField';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import ClockIcon from '@mui/icons-material/Clock';
import { TimePicker } from '@mui/x-date-pickers/TimePicker';

interface TimePickerWrapperProps {
  value?: string;
  onChange: (time: string) => void;
  format?: string;
}

const TimePickerWrapper: React.FC<TimePickerWrapperProps> = (props) => {
  const { value = '', onChange, format = 'hh:mm:ss', ...otherProps } = props;
  const [time, setTime] = useState(value); // Store time with AM/PM

  const handleChange = (newTime: Date | null) => {
    if (newTime !== null) {
      const formattedTime = newTime.toLocaleTimeString([], {
        hour: format.includes('h') ? '2-digit' : undefined,
        minute: '2-digit',
        second: format.includes('s') ? '2-digit' : undefined,
      });
      const convertedTime = convertTime(formattedTime); // Convert to 24-hour format
      setTime(formattedTime);
      onChange(convertedTime);
    }
  };

  const convertTime = (originalTime: string): string => {
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

  const [open, setOpen] = useState(false);
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
