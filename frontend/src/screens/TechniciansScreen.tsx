import type { TechnicianDto } from '@/api/api';
import type { ColumnDef } from '@tanstack/react-table';
import { DataTable, AuthLayout, DeleteDialog } from '@/components';
import { useTranslation } from 'react-i18next';
import { Button } from '@/components/ui/button';
import { useCallback, useState } from 'react';
import { Dialog } from '@/components/Dialog';
import { TechnicianForm } from '@/components/forms/TechnicianForm';
import { useTechnicians, useAuth } from '@/hooks';
import { useDeleteTechnician } from '@/hooks/useDeleteTechnician';
function TechnicianActions({ tech, onEdit, onDelete, t }: {
  tech: TechnicianDto;
  onEdit: (tech: TechnicianDto) => void;
  onDelete: (tech: TechnicianDto) => void;
  t: (key: string) => string;
}) {
  const { tokenPayload } = useAuth();
  if (tokenPayload?.role !== 'ADMIN') return null;
  return (
    <div className="flex gap-2">
      <Button
        type="button"
        variant="ghost"
        className="text-blue-600 hover:underline px-0"
        onClick={() => onEdit(tech)}
      >
        {t('technicians.table.edit')}
      </Button>
      <Button
        type="button"
        variant="ghost"
        className="text-red-600 hover:underline px-0"
        onClick={() => onDelete(tech)}
      >
        {t('technicians.table.delete')}
      </Button>
    </div>
  );
}


export function TechniciansScreen() {
  const { t } = useTranslation();
  const { data, isLoading, error } = useTechnicians();
  const { tokenPayload } = useAuth();
  const isAdmin = tokenPayload?.role === 'ADMIN';
  const [dialog, setDialog] = useState<
    | { type: 'edit'; entity: TechnicianDto | null }
    | { type: 'delete'; entity: TechnicianDto }
    | undefined
  >(undefined);
  const { mutate: deleteTechnician, isPending: isDeleting } = useDeleteTechnician();

  const openDialog = useCallback((technician?: TechnicianDto) => {
    setDialog({ type: 'edit', entity: technician ?? null });
  }, []);

  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);

  const handleDeleteClick = useCallback((tech: TechnicianDto) => {
    setDialog({ type: 'delete', entity: tech });
  }, []);

  const handleDeleteConfirm = useCallback(() => {
    if (dialog?.type === 'delete' && dialog.entity?.id) {
      deleteTechnician(dialog.entity.id, {
        onSuccess: () => setDialog(undefined),
      });
    }
  }, [dialog, deleteTechnician]);


  const columns: ColumnDef<TechnicianDto, unknown>[] = [
    { accessorKey: 'fullName', header: t('technicians.table.fullName'), cell: ({ row }) => row.original.fullName },
    { accessorKey: 'email', header: t('technicians.table.email'), cell: ({ row }) => row.original.email },
    { accessorKey: 'specializations', header: t('technicians.table.specializations'), cell: ({ row }) => {
      const specs = Array.isArray(row.original.specializations)
        ? row.original.specializations
        : Array.from(row.original.specializations || []);
      return (
        <div className="whitespace-pre-line">
          {specs.map((s: string, idx: number) => (
            <span key={s}>
              {s}
              {idx !== specs.length - 1 && '\n'}
            </span>
          ))}
        </div>
      );
    } },
    ...(isAdmin ? [{
      id: 'actions',
      header: t('technicians.table.actions'),
      cell: ({ row }: { row: { original: TechnicianDto } }) => (
        <TechnicianActions
          tech={row.original}
          onEdit={openDialog}
          onDelete={handleDeleteClick}
          t={t}
        />
      ),
      enableSorting: false,
      enableHiding: false,
    }] : []),
  ];

  return (
    <AuthLayout>
      <div className="max-w-4xl mx-auto mt-8 px-4">
        <h1 className="text-2xl font-bold mb-4">{t('technicians.title')}</h1>
        {isLoading && <div>{t('table.loading')}</div>}
        {error && <div className="text-red-500">{t('table.error', { message: error.message })}</div>}
        {data && (
          <DataTable
            columns={columns}
            data={data}
            filterKey="fullName"
            filterPlaceholder={t('technicians.table.filterPlaceholder')}
            openForm={() => openDialog()}
          />
        )}
      </div>
      <DeleteDialog
        open={dialog?.type === 'delete'}
        onOpenChange={open => !open && closeDialog()}
        title={t('form.delete')}
        description={t('form.deleteConfirm')}
        onDelete={handleDeleteConfirm}
        onCancel={closeDialog}
        isDeleting={isDeleting}
      >
        {dialog?.type === 'delete' && dialog.entity && (
          <div className="mt-2 text-sm text-gray-600">
            {dialog.entity.fullName} ({dialog.entity.email})
          </div>
        )}
      </DeleteDialog>
      <Dialog
        open={dialog?.type === 'edit'}
        onOpenChange={open => !open && closeDialog()}
        title={dialog?.entity ? t('technicians.form.editTitle') : t('technicians.form.createTitle')}
      >
        <TechnicianForm
          initialValues={{
            fullName: dialog?.entity?.fullName ?? '',
            email: dialog?.entity?.email ?? '',
            password: '',
            specializations: dialog?.entity?.specializations ?? [],
          }}
          onSuccess={closeDialog}
          id={dialog?.entity?.id}
        />
      </Dialog>
    </AuthLayout>
  );
}
