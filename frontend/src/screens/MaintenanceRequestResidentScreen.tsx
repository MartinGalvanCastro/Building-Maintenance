
import { useCurrentUser } from '@/hooks';
import type { MaintenanceRequestSummaryDto } from '@/api/api';
import { AuthLayout, DataTable } from '@/components';
import { Button } from '@/components/ui/button';
import type { ColumnDef } from '@tanstack/react-table';
import { useTranslation } from 'react-i18next';
import { useState, useCallback, useMemo } from 'react';
import { Dialog } from '@/components/Dialog';
import { DeleteDialog } from '@/components/DeleteDialog';
import { MaintenanceRequestResidentForm } from '@/components/forms/MaintenanceRequestResidentForm';
import { useMaintenanceRequestCancel } from '@/hooks/useMaintenanceRequestCancel';

export function MaintenanceRequestResidentScreen() {
  const { t } = useTranslation();
  const { data: user, isLoading, error } = useCurrentUser();
  const [dialog, setDialog] = useState<
    | { type: 'edit'; entity: MaintenanceRequestSummaryDto | null }
    | { type: 'delete'; entity: MaintenanceRequestSummaryDto }
    | undefined
  >(undefined);
  const { mutate: cancelRequest, isPending: isCancelling } = useMaintenanceRequestCancel({
    residentId: user?.id ?? '',
    onSuccess: () => setDialog(undefined),
  });
  const openDialog = useCallback((request?: MaintenanceRequestSummaryDto) => {
    setDialog({ type: 'edit', entity: request ?? null });
  }, []);
  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);
  const handleCancelClick = useCallback((request: MaintenanceRequestSummaryDto) => {
    setDialog({ type: 'delete', entity: request });
  }, []);
  const handleCancelConfirm = useCallback(() => {
    if (dialog?.type === 'delete' && dialog.entity?.id) {
      cancelRequest(dialog.entity.id);
    }
  }, [dialog, cancelRequest]);

  const requests = useMemo<MaintenanceRequestSummaryDto[]>(() => user?.maintenanceRequests ?? [], [user]);

  const columns: ColumnDef<MaintenanceRequestSummaryDto, unknown>[] = [
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
      id: 'actions',
      header: t('maintenanceRequests.table.actions'),
      cell: ({ row }) => (
        <div className="flex flex-col gap-1 items-stretch">
          <Button
            variant="link"
            className="text-blue-600 p-0 h-auto min-w-0 justify-start"
            onClick={() => openDialog(row.original)}
            disabled={row.original.status === 'CANCELLED' || row.original.status === 'COMPLETED'}
          >
            {t('maintenanceRequests.table.edit')}
          </Button>
          {row.original.status !== 'CANCELLED' && row.original.status !== 'COMPLETED' && (
            <Button
              variant="link"
              className="text-red-600 p-0 h-auto min-w-0 justify-start"
              onClick={() => handleCancelClick(row.original)}
            >
              {t('maintenanceRequests.table.cancel')}
            </Button>
          )}
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
        {requests && (
          <DataTable
            columns={columns}
            data={requests}
            filterKey="description"
            filterPlaceholder={t('maintenanceRequests.table.filterPlaceholder')}
            openForm={() => openDialog()}
          />
        )}
      </div>
      <DeleteDialog
        open={dialog?.type === 'delete'}
        onOpenChange={open => !open && closeDialog()}
        title={t('maintenanceRequests.cancelDialog.title')}
        description={t('maintenanceRequests.cancelDialog.description')}
        onDelete={handleCancelConfirm}
        onCancel={closeDialog}
        isDeleting={isCancelling}
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
        {user && (
          <MaintenanceRequestResidentForm
            {...(dialog?.entity?.id
              ? {
                  id: dialog.entity.id,
                  initialValues: {
                    description: dialog.entity.description ?? '',
                    scheduledAt: dialog.entity.scheduledAt ?? '',
                  },
                  residentId: user.id ?? '',
                }
              : {
                  initialValues: {
                    description: dialog?.entity?.description ?? '',
                    specialization: dialog?.entity?.specialization ?? '',
                    scheduledAt: dialog?.entity?.scheduledAt ?? '',
                  },
                  residentId: user.id ?? '',
                })}
            onSuccess={closeDialog}
          />
        )}
      </Dialog>
    </AuthLayout>
  );
}
