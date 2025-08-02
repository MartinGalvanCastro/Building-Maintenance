import { useTranslation } from 'react-i18next';
import { useTechnicians } from '@/hooks';
import { useAssignTechnician } from '@/hooks/useAssignTechnician';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { Label } from '../ui/label';
import { Button } from '../ui/button';




interface AssignTechnicianFormProps {
  requestId: string;
  onSuccess: () => void;
}



export function AssignTechnicianForm({ requestId, onSuccess }: AssignTechnicianFormProps) {
  const { t } = useTranslation();
  const { data: technicians, isLoading } = useTechnicians();
  const { mutate, isPending } = useAssignTechnician(requestId, onSuccess);

  return (
    <Formik
      initialValues={{ technicianId: '' }}
      validationSchema={Yup.object({
        technicianId: Yup.string().required('maintenanceRequests.form.technicianRequired'),
      })}
      onSubmit={({ technicianId }) => mutate(technicianId)}
    >
      {({ errors, touched, isValid, dirty, submitCount }) => (
        <Form className="space-y-4">
          <div>
            <Label className="block mb-1">{t('maintenanceRequests.form.technician')}</Label>
            <Field
              as="select"
              name="technicianId"
              className="w-full border rounded px-3 py-2"
              disabled={isLoading || isPending}
            >
              <option value="" disabled>{isLoading ? t('form.loading') : t('maintenanceRequests.form.selectTechnician')}</option>
              {(Array.isArray(technicians) ? technicians : []).filter(tech => !!tech.id).map(tech => (
                <option key={tech.id} value={tech.id!}>{tech.fullName}</option>
              ))}
            </Field>
            {errors.technicianId && touched.technicianId && (
              <div className="text-red-500 text-xs mt-1">{t(errors.technicianId)}</div>
            )}
          </div>
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0)}>
            {isPending ? t('form.saving') : t('maintenanceRequests.form.assign')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
