import { Dialog } from '@/components/Dialog';
import { useResidents, useDeleteResident, useResidentialComplexes } from '@/hooks';
import { DataTable, AuthLayout, DeleteDialog } from '@/components';
import type { ResidentDto } from '@/api/api';
import type { ColumnDef } from '@tanstack/react-table';
import { useTranslation } from 'react-i18next';
import { Button } from '@/components/ui/button';
import { ResidentForm } from '@/components/forms';
import { useState, useCallback } from 'react';


export function ResidentsScreen() {
  const { t } = useTranslation();
  const { data, isLoading, error } = useResidents();
  const { data: complexes } = useResidentialComplexes();
  const deleteResident = useDeleteResident();
  const [dialog, setDialog] = useState<
    | { type: 'edit'; entity: ResidentDto | null }
    | { type: 'delete'; entity: ResidentDto }
    | undefined
  >(undefined);

  const openDialog = useCallback((resident?: ResidentDto) => {
    setDialog({ type: 'edit', entity: resident ?? null });
  }, []);
  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);
  const handleDeleteClick = useCallback((resident: ResidentDto) => {
    setDialog({ type: 'delete', entity: resident });
  }, []);
  const handleDeleteConfirm = useCallback(async () => {
    if (dialog?.type === 'delete' && dialog.entity?.id) {
      await deleteResident.mutateAsync(dialog.entity.id);
      setDialog(undefined);
    }
  }, [dialog, deleteResident]);

  const columns: ColumnDef<ResidentDto, unknown>[] = [
    { accessorKey: 'fullName', header: t('residents.table.fullName'), cell: ({ row }) => row.original.fullName },
    { accessorKey: 'email', header: t('residents.table.email'), cell: ({ row }) => row.original.email },
    { accessorKey: 'unitNumber', header: t('residents.table.unitNumber'), cell: ({ row }) => row.original.unitNumber },
    { accessorKey: 'unitBlock', header: t('residents.table.unitBlock'), cell: ({ row }) => row.original.unitBlock },
    { accessorKey: 'residentialComplex', header: t('residents.table.complex'), cell: ({ row }) => row.original.residentialComplex?.name || '' },
    {
      id: 'actions',
      header: t('residents.table.actions'),
      cell: ({ row }) => {
        const resident = row.original;
        return (
          <div className="flex gap-2">
            <Button
              type="button"
              variant="ghost"
              className="text-blue-600 hover:underline px-0"
              onClick={() => openDialog(resident)}
            >
              {t('residents.table.edit')}
            </Button>
            <Button
              type="button"
              variant="ghost"
              className="text-red-600 hover:underline px-0"
              onClick={() => handleDeleteClick(resident)}
            >
              {t('residents.table.delete')}
            </Button>
          </div>
        );
      },
      enableSorting: false,
      enableHiding: false,
    },
  ];

  return (
    <AuthLayout>
      <div className="max-w-4xl mx-auto mt-8 px-4">
        <h1 className="text-2xl font-bold mb-4">{t('residents.title')}</h1>
        {isLoading && <div>{t('table.loading')}</div>}
        {error && <div className="text-red-500">{t('table.error', { message: error.message })}</div>}
        {data && (
          <DataTable
            columns={columns}
            data={data}
            filterKey="fullName"
            filterPlaceholder={t('residents.table.filterPlaceholder')}
            openForm={() => openDialog()}
          />
        )}
      </div>
      <DeleteDialog
        open={dialog?.type === 'delete'}
        onOpenChange={open => !open && closeDialog()}
        title={t('residents.deleteDialog.title')}
        description={t('residents.deleteDialog.description')}
        onDelete={handleDeleteConfirm}
        onCancel={closeDialog}
        isDeleting={deleteResident.isPending}
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
        title={dialog?.entity ? t('residents.form.editTitle') : t('residents.form.createTitle')}
      >
        {complexes && (
          <ResidentForm
            initialValues={{
              fullName: dialog?.entity?.fullName ?? '',
              email: dialog?.entity?.email ?? '',
              unitNumber: dialog?.entity?.unitNumber ?? '',
              unitBlock: dialog?.entity?.unitBlock ?? '',
              password: dialog?.entity ? undefined : '',
              residentialComplexId: dialog?.entity?.residentialComplex?.id ?? '',
            }}
            onSuccess={closeDialog}
          />
        )}
      </Dialog>
    </AuthLayout>
  );
}
