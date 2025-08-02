
import { Formik, Form, Field } from 'formik';
import type { FormikErrors, FormikTouched } from 'formik';
import * as Yup from 'yup';
import { useTranslation } from 'react-i18next';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { DatePicker } from '../ui/date-picker';
import { Label } from '../ui/label';
import { useMaintenanceRequestMutation } from '@/hooks/useMaintenanceRequestMutation';
import { useQuery } from '@tanstack/react-query';
import { listResidents } from '@/services/residentService';
import {
  CreateMaintenanceRequestDtoSpecializationEnum,
} from '@/api/api';


const SPECIALIZATION_OPTIONS = Object.values(CreateMaintenanceRequestDtoSpecializationEnum);




export interface MaintenanceRequestAdminFormCreateValues {
  description: string;
  specialization: string;
  scheduledAt: string;
  residentId: string;
}

export interface MaintenanceRequestAdminFormEditValues {
  description: string;
  scheduledAt: string;
}




type Props =
  | { id?: undefined; initialValues: MaintenanceRequestAdminFormCreateValues; onSuccess: () => void }
  | { id: string; initialValues: MaintenanceRequestAdminFormEditValues; onSuccess: () => void };

const createValidationSchema = Yup.object({
  description: Yup.string().required('maintenanceRequests.form.descriptionRequired'),
  specialization: Yup.string().oneOf(SPECIALIZATION_OPTIONS).required('maintenanceRequests.form.specializationRequired'),
  scheduledAt: Yup.string().required('maintenanceRequests.form.scheduledAtRequired'),
  residentId: Yup.string().required('maintenanceRequests.form.residentRequired'),
});

const editValidationSchema = Yup.object({
  description: Yup.string().required('maintenanceRequests.form.descriptionRequired'),
  scheduledAt: Yup.string().required('maintenanceRequests.form.scheduledAtRequired'),
});

export function MaintenanceRequestAdminForm({ id, initialValues, onSuccess }: Props) {
  const { data: residentsData, isLoading: isResidentsLoading } = useQuery({
    queryKey: ['residents'],
    queryFn: listResidents,
    enabled: !id, // Only fetch for create
  });
  const { t } = useTranslation();
  const isEdit = id !== undefined;
  const { mutate, isPending } = useMaintenanceRequestMutation({
    requestId: id,
    role: 'ADMIN',
    onSuccess
  });
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={isEdit ? editValidationSchema : createValidationSchema}
      onSubmit={(values) => mutate(values)}
      enableReinitialize
    >
      {({ errors, touched, isValid, dirty, submitCount, values, setFieldValue }) => (
        <Form className="space-y-4">
          <div>
            <Label className="block mb-1">{t('maintenanceRequests.form.description')}<span className="text-red-500 ml-1">*</span></Label>
            <Field as={Input} name="description" />
            {errors.description && touched.description && <div className="text-red-500 text-xs mt-1">{t(errors.description)}</div>}
          </div>
          {!isEdit && (() => {
            const createErrors = errors as FormikErrors<MaintenanceRequestAdminFormCreateValues>;
            const createTouched = touched as FormikTouched<MaintenanceRequestAdminFormCreateValues>;
            return (
              <div>
                <Label className="block mb-1">{t('maintenanceRequests.form.specialization')}<span className="text-red-500 ml-1">*</span></Label>
                <Field as="select" name="specialization" className="w-full border rounded px-3 py-2">
                  <option value="" disabled>{t('maintenanceRequests.form.selectSpecialization')}</option>
                  {SPECIALIZATION_OPTIONS.map(option => (
                    <option key={option} value={option}>{t(`maintenanceRequests.specialization.${option.toLowerCase()}`)}</option>
                  ))}
                </Field>
                {createErrors.specialization && createTouched.specialization && <div className="text-red-500 text-xs mt-1">{t(createErrors.specialization)}</div>}
              </div>
            );
          })()}
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
          {!isEdit && (() => {
            const createErrors = errors as FormikErrors<MaintenanceRequestAdminFormCreateValues>;
            const createTouched = touched as FormikTouched<MaintenanceRequestAdminFormCreateValues>;
            return (
              <div>
                <Label className="block mb-1">{t('maintenanceRequests.form.resident')}<span className="text-red-500 ml-1">*</span></Label>
                <Field as="select" name="residentId" className="w-full border rounded px-3 py-2" disabled={isResidentsLoading}>
                  <option value="" disabled>{isResidentsLoading ? t('form.loading') : t('maintenanceRequests.form.selectResident')}</option>
                  {(Array.isArray(residentsData) ? residentsData : []).map((resident) => (
                    <option key={resident.id} value={resident.id}>{resident.fullName}</option>
                  ))}
                </Field>
                {createErrors.residentId && createTouched.residentId && <div className="text-red-500 text-xs mt-1">{t(createErrors.residentId)}</div>}
              </div>
            );
          })()}
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0)}>
            {isPending ? t('form.saving') : t(isEdit ? 'form.save' : 'form.create')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
