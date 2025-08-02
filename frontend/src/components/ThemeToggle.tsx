import { useTheme } from 'next-themes';
import { useEffect, useState } from 'react';
import { Switch } from './ui/switch';
import { SunIcon, MoonIcon } from 'lucide-react';


export function ThemeToggle() {
  const { setTheme, resolvedTheme } = useTheme();
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) return null;

  return (
    <div className="fixed bottom-4 right-4 z-50">
      <div className="flex items-center justify-center gap-3 bg-card border border-border rounded-lg shadow-lg px-4 py-2">
        <div className="flex items-center justify-center h-7 text-sm font-medium relative">
          <div className="relative flex items-center justify-center w-24">
            <span className="pointer-events-none flex min-w-8 items-center justify-center text-center transition-transform duration-300 ease-[cubic-bezier(0.16,1,0.3,1)] peer-data-[state=checked]:invisible peer-data-[state=unchecked]:translate-x-6 peer-data-[state=unchecked]:rtl:-translate-x-6">
              <SunIcon className="size-4" aria-hidden="true" />
            </span>
            <Switch
              checked={resolvedTheme === 'dark'}
              onCheckedChange={(checked) => setTheme(checked ? 'dark' : 'light')}
              className="peer data-[state=unchecked]:bg-input/50 absolute left-1/2 -translate-x-1/2 h-[inherit] w-14 [&_span]:z-10 [&_span]:size-6.5 [&_span]:transition-transform [&_span]:duration-300 [&_span]:ease-[cubic-bezier(0.16,1,0.3,1)] [&_span]:data-[state=checked]:translate-x-7 [&_span]:data-[state=checked]:rtl:-translate-x-7"
              aria-label="Toggle dark mode"
            />
            <span className="peer-data-[state=checked]:text-background pointer-events-none flex min-w-8 items-center justify-center text-center transition-transform duration-300 ease-[cubic-bezier(0.16,1,0.3,1)] peer-data-[state=checked]:-translate-x-full peer-data-[state=unchecked]:invisible peer-data-[state=checked]:rtl:translate-x-full">
              <MoonIcon className="size-4" aria-hidden="true" />
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
