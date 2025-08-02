import { Dialog } from '@/components/Dialog';
import { Button } from '@/components/ui/button';
import { useTranslation } from 'react-i18next';
import type { ReactNode } from 'react';

interface DeleteDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title?: string;
  description?: string;
  onDelete: () => void;
  onCancel?: () => void;
  isDeleting?: boolean;
  children?: ReactNode;
}

export function DeleteDialog({
  open,
  onOpenChange,
  title,
  description,
  onDelete,
  onCancel,
  isDeleting,
  children,
}: DeleteDialogProps) {
  const { t } = useTranslation();
  return (
    <Dialog open={open} onOpenChange={onOpenChange} title={title ?? t('form.delete')}>
      <div className="p-4">
        <p>{description ?? t('form.deleteConfirm')}</p>
        {children}
        <div className="flex gap-2 mt-4 justify-end">
          <Button
            variant="secondary"
            type="button"
            onClick={onCancel ?? (() => onOpenChange(false))}
            disabled={isDeleting}
          >
            {t('form.cancel')}
          </Button>
          <Button
            variant="destructive"
            type="button"
            onClick={onDelete}
            disabled={isDeleting}
          >
            {isDeleting ? t('form.saving') : t('form.delete')}
          </Button>
        </div>
      </div>
    </Dialog>
  );
}
