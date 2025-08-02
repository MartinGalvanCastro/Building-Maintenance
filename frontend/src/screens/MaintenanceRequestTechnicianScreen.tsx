
import { useCurrentUser } from '@/hooks';
import type { MaintenanceRequestSummaryDto } from '@/api/api';
import { AuthLayout, DataTable } from '@/components';
import { Button } from '@/components/ui/button';
import type { ColumnDef } from '@tanstack/react-table';
import { useTranslation } from 'react-i18next';
import { useState, useCallback } from 'react';
import { Dialog } from '@/components/Dialog';
import { TechnicianStatusUpdateForm } from '@/components/forms/TechnicianStatusUpdateForm';

export function MaintenanceRequestTechnicianScreen() {
  const { t } = useTranslation();
  const { data: user, isLoading, error } = useCurrentUser();
  const [dialog, setDialog] = useState<
    | { type: 'status'; entity: MaintenanceRequestSummaryDto }
    | undefined
  >(undefined);


  const openStatusDialog = useCallback((request: MaintenanceRequestSummaryDto) => {
    setDialog({ type: 'status', entity: request });
  }, []);
  const closeDialog = useCallback(() => {
    setDialog(undefined);
  }, []);

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
      id: 'location',
      header: t('maintenanceRequests.table.location'),
      cell: ({ row }) => {
        const complex = row.original.resident?.residentialComplex;
        const city = complex?.city || '';
        const address = complex?.address || '';
        const postal = complex?.postalCode || '';
        if (address && city && postal) return `${address}, ${city} (${postal})`;
        if (address && city) return `${address}, ${city}`;
        if (address && postal) return `${address} (${postal})`;
        if (city && postal) return `${city} (${postal})`;
        return address || city || postal || '-';
      },
      enableSorting: false,
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
      cell: ({ row }) => {
        const status = row.original.status;
        const isDisabled = status === 'COMPLETED' || status === 'CANCELLED';
        return (
          <Button
            variant="link"
            className="text-blue-600 p-0 h-auto min-w-0 justify-start"
            onClick={() => openStatusDialog(row.original)}
            disabled={isDisabled}
          >
            {t('maintenanceRequests.table.updateStatus')}
          </Button>
        );
      },
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
        <DataTable columns={columns} data={user?.maintenanceRequests ?? []} filterKey="description" filterPlaceholder={t('maintenanceRequests.table.filterPlaceholder')} />
      </div>
      <Dialog
        open={dialog?.type === 'status'}
        onOpenChange={open => !open && closeDialog()}
        title={t('maintenanceRequests.statusDialog.title')}
      >
        {dialog?.type === 'status' && user && (
          <TechnicianStatusUpdateForm
            technicianId={user.id ?? ''}
            requestId={dialog.entity.id ?? ''}
            initialStatus={dialog.entity.status ?? ''}
            onSuccess={closeDialog}
          />
        )}
      </Dialog>
    </AuthLayout>
  );
}
