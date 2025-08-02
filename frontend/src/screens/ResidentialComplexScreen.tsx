import { useResidentialComplexes, useAuth } from '@/hooks';
import { DataTable, AuthLayout } from '@/components';
import type { ResidentialComplexDto } from '@/api/api';
import type { ColumnDef } from '@tanstack/react-table';
import { useTranslation } from 'react-i18next';
import { Button } from '@/components/ui/button';
import { useCallback, useState } from 'react';
import { Dialog } from '@/components/Dialog';
import { ResidentialComplexForm } from '@/components/forms/ResidentialComplexForm';
import { DeleteDialog } from '@/components/DeleteDialog';
import { useDeleteResidentialComplex } from '@/hooks/useDeleteResidentialComplex';

function ResidentialComplexActions({ complex, onEdit, onDelete, t }: {
  complex: ResidentialComplexDto;
  onEdit: (complex: ResidentialComplexDto) => void;
  onDelete: (complex: ResidentialComplexDto) => void;
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
        onClick={() => onEdit(complex)}
      >
        {t('residentialComplex.table.edit')}
      </Button>
      <Button
        type="button"
        variant="ghost"
        className="text-red-600 hover:underline px-0"
        onClick={() => onDelete(complex)}
      >
        {t('residentialComplex.table.delete')}
      </Button>
    </div>
  );
}

export function ResidentialComplexScreen() {
  const { t } = useTranslation();
  const { data, isLoading, error } = useResidentialComplexes();
  const { tokenPayload } = useAuth();
  const isAdmin = tokenPayload?.role === 'ADMIN';
  const [dialog, setDialog] = useState<
    | { type: 'edit'; entity: ResidentialComplexDto | null }
    | { type: 'delete'; entity: ResidentialComplexDto }
    | undefined
  >(undefined);
  const { mutate: deleteComplex, isPending: isDeleting } = useDeleteResidentialComplex();

  const openDialog = useCallback((complex?: ResidentialComplexDto) => {
    setDialog({ type: 'edit', entity: complex ?? null });
  }, []);

  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);

  const handleDeleteClick = useCallback((complex: ResidentialComplexDto) => {
    setDialog({ type: 'delete', entity: complex });
  }, []);

  const handleDeleteConfirm = useCallback(() => {
    if (dialog?.type === 'delete' && dialog.entity?.id) {
      deleteComplex(dialog.entity.id, {
        onSuccess: () => setDialog(undefined),
      });
    }
  }, [dialog, deleteComplex]);


  const columns: ColumnDef<ResidentialComplexDto, unknown>[] = [
    { accessorKey: 'name', header: t('residentialComplex.table.name'), cell: ({ row }) => row.original.name },
    { accessorKey: 'address', header: t('residentialComplex.table.address'), cell: ({ row }) => row.original.address },
    { accessorKey: 'city', header: t('residentialComplex.table.city'), cell: ({ row }) => row.original.city },
    { accessorKey: 'postalCode', header: t('residentialComplex.table.postalCode'), cell: ({ row }) => row.original.postalCode },
    ...(isAdmin ? [{
      id: 'actions',
      header: t('residentialComplex.table.actions'),
      cell: ({ row }: { row: { original: ResidentialComplexDto } }) => (
        <ResidentialComplexActions
          complex={row.original}
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
        <h1 className="text-2xl font-bold mb-4">{t('residentialComplex.title')}</h1>
        {isLoading && <div>{t('table.loading')}</div>}
        {error && <div className="text-red-500">{t('table.error', { message: error.message })}</div>}
        {data && (
          <DataTable
            columns={columns}
            data={data}
            filterKey="name"
            filterPlaceholder={t('residentialComplex.table.filterPlaceholder')}
            openForm={openDialog}
          />
        )}
      </div>
      <DeleteDialog
        open={dialog?.type === 'delete'}
        onOpenChange={open => !open && setDialog(undefined)}
        title={t('form.delete')}
        description={t('form.deleteConfirm')}
        onDelete={handleDeleteConfirm}
        onCancel={closeDialog}
        isDeleting={isDeleting}
      >
        {dialog?.type === 'delete' && dialog.entity && (
          <div className="mt-2 text-sm text-gray-600">
            {dialog.entity.name} ({dialog.entity.address})
          </div>
        )}
      </DeleteDialog>
      <Dialog
        open={dialog?.type === 'edit'}
        onOpenChange={open => !open && setDialog(undefined)}
        title={dialog?.entity ? t('residentialComplex.form.editTitle') : t('residentialComplex.form.createTitle')}
      >
        <ResidentialComplexForm
          initialValues={{
            name: dialog?.entity?.name ?? '',
            address: dialog?.entity?.address ?? '',
            city: dialog?.entity?.city ?? '',
            postalCode: dialog?.entity?.postalCode ?? '',
          }}
          id={dialog?.entity?.id}
          onSuccess={closeDialog}
          onCancel={closeDialog}
        />
      </Dialog>
    </AuthLayout>
  );
}
