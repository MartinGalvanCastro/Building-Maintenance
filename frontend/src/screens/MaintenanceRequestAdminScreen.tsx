import { useMaintenanceRequests } from '@/hooks';
import type { MaintenanceRequestDto } from '@/api/api';
import { AuthLayout, DataTable } from '@/components';
import { Button } from '@/components/ui/button';
import type { ColumnDef } from '@tanstack/react-table';
import { useTranslation } from 'react-i18next';
import { useState, useCallback } from 'react';
import { Dialog } from '@/components/Dialog';
import { DeleteDialog } from '@/components/DeleteDialog';
import { MaintenanceRequestAdminForm } from '@/components/forms/MaintenanceRequestAdminForm';
import { AssignTechnicianForm } from '@/components/forms/AssignTechnicianForm';
import { useDeleteMaintenanceRequest } from '@/hooks/useDeleteMaintenanceRequest';


export function MaintenanceRequestAdminScreen() {
  const { t } = useTranslation();
  const { data, isLoading, error } = useMaintenanceRequests();
  const [dialog, setDialog] = useState<
    | { type: 'edit'; entity: MaintenanceRequestDto | null }
    | { type: 'delete'; entity: MaintenanceRequestDto }
    | { type: 'assignTechnician'; entity: MaintenanceRequestDto }
    | undefined
  >(undefined);
  const { mutate: deleteRequest, isPending: isDeleting } = useDeleteMaintenanceRequest();
  const openDialog = useCallback((request?: MaintenanceRequestDto) => {
    setDialog({ type: 'edit', entity: request ?? null });
  }, []);
  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);
  const handleDeleteClick = useCallback((request: MaintenanceRequestDto) => {
    setDialog({ type: 'delete', entity: request });
  }, []);
  const handleDeleteConfirm = useCallback(() => {
    if (dialog?.type === 'delete' && dialog.entity?.id) {
      deleteRequest(dialog.entity.id, {
        onSuccess: () => setDialog(undefined),
      });
    }
  }, [dialog, deleteRequest]);
  const columns: ColumnDef<MaintenanceRequestDto, unknown>[] = [
    {
      accessorKey: 'description',
      header: t('maintenanceRequests.table.description'),
      cell: ({ row }) => row.original.description,
      enableSorting: true,
    },
    {
      accessorKey: 'specialization',
      header: t('maintenanceRequests.table.specialization'),
      cell: ({ row }) => row.original.specialization,
      enableSorting: true,
    },
    {
      accessorKey: 'status',
      header: t('maintenanceRequests.table.status'),
      cell: ({ row }) => row.original.status,
      enableSorting: true,
    },
    {
      accessorKey: 'createdAt',
      header: t('maintenanceRequests.table.createdAt'),
      cell: ({ row }) => row.original.createdAt ? new Date(row.original.createdAt).toLocaleString() : '',
      enableSorting: true,
    },
    {
      accessorKey: 'scheduledAt',
      header: t('maintenanceRequests.table.scheduledAt'),
      cell: ({ row }) => row.original.scheduledAt ? new Date(row.original.scheduledAt).toLocaleString() : '',
      enableSorting: true,
    },
    {
      accessorKey: 'completedAt',
      header: t('maintenanceRequests.table.completedAt'),
      cell: ({ row }) => row.original.completedAt ? new Date(row.original.completedAt).toLocaleString() : '',
      enableSorting: true,
    },
    {
      accessorKey: 'resident',
      header: t('maintenanceRequests.table.resident'),
      cell: ({ row }) => row.original.resident?.fullName || '',
      enableSorting: false,
    },
    {
      accessorKey: 'technician',
      header: t('maintenanceRequests.table.technician'),
      cell: ({ row }) => row.original.technician?.fullName || '',
      enableSorting: false,
    },
    {
      id: 'actions',
      header: t('maintenanceRequests.table.actions'),
      cell: ({ row }) => (
        <div className="flex flex-col gap-1 items-stretch">
          <Button
            variant="link"
            className="text-blue-600 p-0 h-auto min-w-0 justify-start"
            onClick={() => openDialog(row.original)}
          >
            {t('maintenanceRequests.table.edit')}
          </Button>
          <Button
            variant="link"
            className="text-red-600 p-0 h-auto min-w-0 justify-start"
            onClick={() => handleDeleteClick(row.original)}
          >
            {t('maintenanceRequests.table.delete')}
          </Button>
          <Button
            variant="link"
            className="text-green-600 p-0 h-auto min-w-0 justify-start"
            onClick={() => setDialog({ type: 'assignTechnician', entity: row.original })}
          >
            {t('maintenanceRequests.table.assignTechnician')}
          </Button>
        </div>
      ),
      enableSorting: false,
      enableHiding: false,
    },
  ];

  return (
    <AuthLayout>
      <div className="max-w-4xl mx-auto mt-8 px-4">
        <h1 className="text-2xl font-bold mb-4">{t('home.maintenance.title')}</h1>
        {isLoading && <div>{t('table.loading')}</div>}
        {error && <div className="text-red-500">{t('table.error', { message: error.message })}</div>}
        {data && (
          <DataTable
            columns={columns}
            data={data}
            filterKey="description"
            filterPlaceholder={t('maintenanceRequests.table.filterPlaceholder')}
            openForm={() => openDialog()}
          />
        )}
      </div>
      <DeleteDialog
        open={dialog?.type === 'delete'}
        onOpenChange={open => !open && closeDialog()}
        title={t('maintenanceRequests.deleteDialog.title')}
        description={t('maintenanceRequests.deleteDialog.description')}
        onDelete={handleDeleteConfirm}
        onCancel={closeDialog}
        isDeleting={isDeleting}
      >
        {dialog?.type === 'delete' && dialog.entity && (
          <div className="mt-2 text-sm text-gray-600">
            {dialog.entity.description}
          </div>
        )}
      </DeleteDialog>
      <Dialog
        open={dialog?.type === 'edit'}
        onOpenChange={open => !open && closeDialog()}
        title={dialog?.entity ? t('maintenanceRequests.form.editTitle') : t('maintenanceRequests.form.createTitle')}
      >
        {dialog?.entity?.id ? (
          <MaintenanceRequestAdminForm
            id={dialog.entity.id}
            initialValues={{
              description: dialog.entity.description ?? '',
              scheduledAt: dialog.entity.scheduledAt ?? '',
            }}
            onSuccess={closeDialog}
          />
        ) : (
          <MaintenanceRequestAdminForm
            initialValues={{
              description: dialog?.entity?.description ?? '',
              specialization: dialog?.entity?.specialization ?? '',
              scheduledAt: dialog?.entity?.scheduledAt ?? '',
              residentId: dialog?.entity?.resident?.id ?? '',
            }}
            onSuccess={closeDialog}
          />
        )}
      </Dialog>
      <Dialog
        open={dialog?.type === 'assignTechnician'}
        onOpenChange={open => !open && closeDialog()}
        title={t('maintenanceRequests.form.assignTechnicianTitle')}
      >
        {dialog?.type === 'assignTechnician' && dialog.entity && (
          <AssignTechnicianForm
            requestId={dialog.entity?.id ?? ''}
            onSuccess={closeDialog}
          />
        )}
      </Dialog>
    </AuthLayout>
  );
}
