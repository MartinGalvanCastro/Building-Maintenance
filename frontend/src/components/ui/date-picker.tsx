import { useState, useEffect } from "react";
import { CalendarIcon } from "lucide-react";
import { Calendar } from "@/components/ui/calendar";
import { Input } from "@/components/ui/input";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";

function formatDate(date: Date | undefined) {
  if (!date) return "";
  return date.toLocaleDateString("en-US", {
    day: "2-digit",
    month: "long",
    year: "numeric",
  });
}


export interface DatePickerProps {
  value: Date | null;
  onChange: (date: Date | null) => void;
  placeholder?: string;
  disabled?: boolean;
  className?: string;
}

export function DatePicker({ value, onChange, placeholder, disabled, className }: DatePickerProps) {
  const [open, setOpen] = useState(false);
  const [inputValue, setInputValue] = useState(value ? formatDate(value) : "");
  const [month, setMonth] = useState<Date | undefined>(value ?? undefined);
  const [isHoveringInput, setIsHoveringInput] = useState(false);
  const [isHoveringPopover, setIsHoveringPopover] = useState(false);
  // Hour/minute state
  const initialHour = value ? value.getHours() : 0;
  const initialMinute = value ? value.getMinutes() : 0;
  const [hour, setHour] = useState<number>(initialHour);
  const [minute, setMinute] = useState<number>(initialMinute);

  useEffect(() => {
    if (!isHoveringInput && !isHoveringPopover && open) {
      setOpen(false);
    }
  }, [isHoveringInput, isHoveringPopover, open]);

  useEffect(() => {
    setInputValue(value ? formatDate(value) : "");
    if (value) {
      setHour(value.getHours());
      setMinute(value.getMinutes());
    }
  }, [value]);

  return (
    <div className={"flex flex-col gap-3 " + (className ?? "") }>
      <div className="relative flex gap-2">
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <div className="relative w-full">
              <span className="absolute left-2 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none">
                <CalendarIcon className="size-4" />
              </span>
              <Input
                value={inputValue}
                placeholder={placeholder}
                className="bg-background pl-9 pr-2 cursor-pointer"
                disabled={disabled}
                readOnly
                tabIndex={0}
                onMouseDown={e => {
                  e.preventDefault();
                  setOpen(true);
                }}
                onMouseEnter={() => {
                  setIsHoveringInput(true);
                  setOpen(true);
                }}
                onMouseLeave={() => setIsHoveringInput(false)}
                onFocus={() => setOpen(true)}
              />
            </div>
          </PopoverTrigger>
          <PopoverContent
            className="w-auto overflow-hidden p-0"
            side="bottom"
            align="start"
            onMouseEnter={() => setIsHoveringPopover(true)}
            onMouseLeave={() => setIsHoveringPopover(false)}
          >
            <Calendar
              mode="single"
              selected={value ?? undefined}
              captionLayout="dropdown"
              month={month}
              onMonthChange={setMonth}
              onSelect={date => {
                if (!date) {
                  onChange(null);
                  setInputValue("");
                  setOpen(false);
                  return;
                }
                // Combine with hour/minute
                const newDate = new Date(date);
                newDate.setHours(hour);
                newDate.setMinutes(minute);
                newDate.setSeconds(0);
                newDate.setMilliseconds(0);
                onChange(newDate);
                setInputValue(formatDate(newDate));
                setOpen(false);
              }}
            />
            {/* Hour/minute picker */}
            <div className="flex items-center gap-2 px-4 py-2 border-t bg-background">
              <select
                className="border rounded px-2 py-1 text-sm"
                value={hour}
                onChange={e => {
                  const newHour = Number(e.target.value);
                  setHour(newHour);
                  if (value) {
                    const newDate = new Date(value);
                    newDate.setHours(newHour);
                    newDate.setMinutes(minute);
                    newDate.setSeconds(0);
                    newDate.setMilliseconds(0);
                    onChange(newDate);
                    setInputValue(formatDate(newDate));
                  }
                }}
              >
                {Array.from({ length: 24 }, (_, i) => (
                  <option key={i} value={i}>{i.toString().padStart(2, '0')}</option>
                ))}
              </select>
              <span className="text-sm">:</span>
              <select
                className="border rounded px-2 py-1 text-sm"
                value={minute}
                onChange={e => {
                  const newMinute = Number(e.target.value);
                  setMinute(newMinute);
                  if (value) {
                    const newDate = new Date(value);
                    newDate.setHours(hour);
                    newDate.setMinutes(newMinute);
                    newDate.setSeconds(0);
                    newDate.setMilliseconds(0);
                    onChange(newDate);
                    setInputValue(formatDate(newDate));
                  }
                }}
              >
                {Array.from({ length: 60 }, (_, i) => (
                  <option key={i} value={i}>{i.toString().padStart(2, '0')}</option>
                ))}
              </select>
            </div>
          </PopoverContent>
        </Popover>
      </div>
    </div>
  );
}
