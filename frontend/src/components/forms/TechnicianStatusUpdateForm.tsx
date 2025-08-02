import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { useTranslation } from 'react-i18next';
import { Button } from '../ui/button';
import { Label } from '../ui/label';
import { useMaintenanceRequestMutation } from '@/hooks/useMaintenanceRequestMutation';

import { ChangeStatusDtoStatusEnum, type ChangeStatusDto } from '@/api/api';

// Do not allow technician to set status to Cancelled
const STATUS_OPTIONS = [
  ChangeStatusDtoStatusEnum.Pending,
  ChangeStatusDtoStatusEnum.Scheduled,
  ChangeStatusDtoStatusEnum.InProgress,
  ChangeStatusDtoStatusEnum.Completed,
];

const validationSchema = Yup.object({
  status: Yup.string().oneOf(STATUS_OPTIONS).required('maintenanceRequests.form.statusRequired'),
});

interface TechnicianStatusUpdateFormProps {
  technicianId: string;
  requestId: string;
  initialStatus: string;
  onSuccess: () => void;
}

export function TechnicianStatusUpdateForm({ technicianId, requestId, initialStatus, onSuccess }: TechnicianStatusUpdateFormProps) {
  const { t } = useTranslation();
  const { mutate, isPending } = useMaintenanceRequestMutation({
    role: 'TECHNICIAN',
    technicianId,
    requestId,
    onSuccess,
  });
  const isFinalStatus = initialStatus === ChangeStatusDtoStatusEnum.Completed || initialStatus === ChangeStatusDtoStatusEnum.Cancelled;
  return (
    <Formik
      initialValues={{ status: initialStatus }}
      validationSchema={validationSchema}
      onSubmit={values => mutate({ status: values.status } as ChangeStatusDto)}
      enableReinitialize
    >
      {({ errors, touched, isValid, dirty, submitCount }) => (
        <Form className="space-y-4">
          <div>
            <Label className="block mb-1">{t('maintenanceRequests.form.status')}<span className="text-red-500 ml-1">*</span></Label>
            <Field as="select" name="status" className="w-full border rounded px-3 py-2" disabled={isFinalStatus}>
              <option value="" disabled>{t('maintenanceRequests.form.selectStatus')}</option>
              {STATUS_OPTIONS.map(option => (
                <option key={option} value={option}>{t(`maintenanceRequests.status.${option.toLowerCase()}`)}</option>
              ))}
            </Field>
            {errors.status && touched.status && <div className="text-red-500 text-xs mt-1">{t(errors.status)}</div>}
          </div>
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0) || isFinalStatus}>
            {isPending ? t('form.saving') : t('form.save')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
