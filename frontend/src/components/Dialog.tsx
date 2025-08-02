import { type ReactNode } from 'react';
import { Dialog as ShadDialog, DialogContent, DialogHeader, DialogTitle } from './ui/dialog';

interface DialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  children: ReactNode;
  className?: string;
}

export function Dialog({ open, onOpenChange, title, children, className }: DialogProps) {
  return (
    <ShadDialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className={className || 'max-w-md w-full'}>
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
        </DialogHeader>
        {children}
      </DialogContent>
    </ShadDialog>
  );
}
