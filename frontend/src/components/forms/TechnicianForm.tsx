import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { Checkbox } from '../ui/checkbox';
import { Label } from '../ui/label';
import { useTranslation } from 'react-i18next';
import { TechnicianCreateCommandDtoSpecializationsEnum } from '@/api/api';
import type { TechnicianDto } from '@/api/api';
import { useTechnicianMutation } from '@/hooks/useTechnicianMutation';


export interface TechnicianFormValues {
  fullName: string;
  email: string;
  password?: string;
  specializations: string[];
}

const validationSchema = (isCreate: boolean) =>
  Yup.object({
    fullName: Yup.string().required('technicians.form.fullNameRequired'),
    email: Yup.string().email('technicians.form.emailInvalid').required('technicians.form.emailRequired'),
    password: isCreate
      ? Yup.string().min(6, 'technicians.form.passwordMin').required('technicians.form.passwordRequired')
      : Yup.string().notRequired(),
    specializations: Yup.array().of(Yup.string().required()).min(1, 'technicians.form.specializationsRequired'),
  });




interface Props {
  initialValues: TechnicianFormValues;
  id?: string;
  onSuccess?: (data: TechnicianDto) => void;
}





export function TechnicianForm({ initialValues, id, onSuccess }: Props) {
  const { t } = useTranslation();
  const isEdit = id !== undefined;
  const { mutate, error, isPending } = useTechnicianMutation(id, onSuccess);
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema(!isEdit)}
      onSubmit={mutate}
      enableReinitialize
    >
      {({ errors, touched, values, setFieldValue, isValid, dirty, submitCount }) => (
        <Form className="space-y-4">
          <div>
            <Label className="mb-1" htmlFor="fullName">
              {t('technicians.table.fullName')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="fullName" id="fullName" />
            {errors.fullName && touched.fullName && <div className="text-red-500 text-xs mt-1">{t(errors.fullName)}</div>}
          </div>
          <div>
            <Label className="mb-1" htmlFor="email">
              {t('technicians.table.email')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="email" id="email" />
            {errors.email && touched.email && <div className="text-red-500 text-xs mt-1">{t(errors.email)}</div>}
          </div>
          {!isEdit && (
            <div>
              <Label className="mb-1" htmlFor="password">
                {t('technicians.form.password')}
                <span className="text-red-500 ml-1">*</span>
              </Label>
              <Field as={Input} name="password" type="password" id="password" autoComplete="new-password" />
              {errors.password && touched.password && <div className="text-red-500 text-xs mt-1">{t(errors.password)}</div>}
            </div>
          )}
          <div>
            <Label className="mb-1" htmlFor="specializations-group">
              {t('technicians.table.specializations')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <div className="flex flex-col gap-2" id="specializations-group">
              {Object.values(TechnicianCreateCommandDtoSpecializationsEnum).map((value) => (
                <div key={value} className="flex items-center gap-2 cursor-pointer">
                  <Checkbox
                    checked={values.specializations.includes(value)}
                    onCheckedChange={checked => {
                      if (checked) {
                        setFieldValue('specializations', [...values.specializations, value]);
                      } else {
                        setFieldValue('specializations', values.specializations.filter((v: string) => v !== value));
                      }
                    }}
                    id={`specializations-${value}`}
                  />
                  <Label htmlFor={`specializations-${value}`} className="cursor-pointer">
                    {t(`technicians.specialization.${value.toLowerCase()}`)}
                  </Label>
                </div>
              ))}
            </div>
            {errors.specializations && touched.specializations && <div className="text-red-500 text-xs mt-1">{t(typeof errors.specializations === 'string' ? errors.specializations : '')}</div>}
          </div>
          {error && typeof error === 'object' && 'message' in error && (
            <div className="text-red-500 text-xs mt-2">{(error as Error).message}</div>
          )}
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0)}>
            {isPending ? t('form.saving') : t(isEdit ? 'form.save' : 'form.create')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
