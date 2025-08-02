import { Formik, Form, Field } from 'formik';
import type { FormikTouched } from 'formik';
import * as Yup from 'yup';
import { useTranslation } from 'react-i18next';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { DatePicker } from '../ui/date-picker';
import { Label } from '../ui/label';
import { useMaintenanceRequestMutation } from '@/hooks/useMaintenanceRequestMutation';
import { useQueryClient } from '@tanstack/react-query';
import { CreateMaintenanceRequestDtoSpecializationEnum } from '@/api/api';

const SPECIALIZATION_OPTIONS = Object.values(CreateMaintenanceRequestDtoSpecializationEnum);

const createValidationSchema = Yup.object({
  description: Yup.string().required('maintenanceRequests.form.descriptionRequired'),
  specialization: Yup.string().required('maintenanceRequests.form.specializationRequired'),
  scheduledAt: Yup.string().required('maintenanceRequests.form.scheduledAtRequired'),
});

const editValidationSchema = Yup.object({
  description: Yup.string().required('maintenanceRequests.form.descriptionRequired'),
  scheduledAt: Yup.string().required('maintenanceRequests.form.scheduledAtRequired'),
});

export interface MaintenanceRequestResidentFormCreateValues {
  description: string;
  specialization: string;
  scheduledAt: string;
}

export interface MaintenanceRequestResidentFormEditValues {
  description: string;
  scheduledAt: string;
}

type Props =
  | { id?: undefined; initialValues: MaintenanceRequestResidentFormCreateValues; residentId: string; onSuccess: () => void }
  | { id: string; initialValues: MaintenanceRequestResidentFormEditValues; residentId: string; onSuccess: () => void };

export function MaintenanceRequestResidentForm(props: Props) {
  const { id, initialValues, residentId, onSuccess } = props;
  const { t } = useTranslation();
  const isEdit = id !== undefined;
  const queryClient = useQueryClient();
  const { mutate, isPending } = useMaintenanceRequestMutation({
    requestId: id,
    role: 'RESIDENT',
    residentId,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['currentUser'] });
      onSuccess();
    },
  });
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={isEdit ? editValidationSchema : createValidationSchema}
      onSubmit={values => mutate(values)}
      enableReinitialize
    >
      {({ errors, touched, isValid, dirty, submitCount, values, setFieldValue }) => (
        <Form className="space-y-4">
          <div>
            <Label className="block mb-1">{t('maintenanceRequests.form.description')}<span className="text-red-500 ml-1">*</span></Label>
            <Field as={Input} name="description" />
            {errors.description && touched.description && <div className="text-red-500 text-xs mt-1">{t(errors.description)}</div>}
          </div>
          {!isEdit && (
            <div>
              <Label className="block mb-1">{t('maintenanceRequests.form.specialization')}<span className="text-red-500 ml-1">*</span></Label>
              <Field as="select" name="specialization" className="w-full border rounded px-3 py-2">
                <option value="" disabled>{t('maintenanceRequests.form.selectSpecialization')}</option>
                {SPECIALIZATION_OPTIONS.map(option => (
                  <option key={option} value={option}>{t(`maintenanceRequests.specialization.${option.toLowerCase()}`)}</option>
                ))}
              </Field>
              {(
                (errors as MaintenanceRequestResidentFormCreateValues).specialization &&
                (touched as FormikTouched<MaintenanceRequestResidentFormCreateValues>).specialization
              ) && (
                <div className="text-red-500 text-xs mt-1">{t((errors as MaintenanceRequestResidentFormCreateValues).specialization!)}</div>
              )}
            </div>
          )}
          <div>
            <Label className="block mb-1">{t('maintenanceRequests.form.scheduledAt')}</Label>
            <DatePicker
              value={values.scheduledAt ? new Date(values.scheduledAt) : null}
              onChange={date => setFieldValue('scheduledAt', date ? date.toISOString() : '')}
              placeholder={t('maintenanceRequests.form.selectDate')}
              disabled={isPending}
            />
            {errors.scheduledAt && touched.scheduledAt && <div className="text-red-500 text-xs mt-1">{t(errors.scheduledAt)}</div>}
          </div>
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0)}>
            {isPending ? t('form.saving') : t(isEdit ? 'form.save' : 'form.create')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
