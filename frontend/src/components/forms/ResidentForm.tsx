import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { useTranslation } from 'react-i18next';
import { useResidentialComplexes } from '@/hooks';
import { useResidentMutation } from '@/hooks/useResidentMutation';

export interface ResidentFormValues {
  fullName: string;
  email: string;
  password?: string;
  unitNumber: string;
  unitBlock: string;
  residentialComplexId: string;
}

const validationSchema = (isCreate: boolean) =>
  Yup.object({
    fullName: Yup.string().required('residents.form.fullNameRequired'),
    email: Yup.string().email('residents.form.emailInvalid').required('residents.form.emailRequired'),
    password: isCreate
      ? Yup.string().min(6, 'residents.form.passwordMin').required('residents.form.passwordRequired')
      : Yup.string().notRequired(),
    unitNumber: Yup.string().required('residents.form.unitNumberRequired'),
    unitBlock: Yup.string().required('residents.form.unitBlockRequired'),
    residentialComplexId: Yup.string().required('residents.form.complexRequired'),
  });




interface ResidentFormProps{
  initialValues: ResidentFormValues;
  id?: string;
  onSuccess?: () => void;
}
  
export function ResidentForm({ initialValues, id, onSuccess }: ResidentFormProps) {
  const isEdit = id !== undefined;
  const { t } = useTranslation();
const { mutate, isPending } = useResidentMutation(id, onSuccess);
const { data: complexes, status } = useResidentialComplexes();
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema(!isEdit)}
      onSubmit={mutate}
      enableReinitialize
    >
      {({ errors, touched, isValid, dirty, submitCount }) => (
        <Form className="space-y-4">
          <div>
            <label className="block mb-1">{t('residents.table.fullName')}<span className="text-red-500 ml-1">*</span></label>
            <Field as={Input} name="fullName" />
            {errors.fullName && touched.fullName && <div className="text-red-500 text-xs mt-1">{t(errors.fullName)}</div>}
          </div>
          <div>
            <label className="block mb-1">{t('residents.table.email')}<span className="text-red-500 ml-1">*</span></label>
            <Field as={Input} name="email" />
            {errors.email && touched.email && <div className="text-red-500 text-xs mt-1">{t(errors.email)}</div>}
          </div>
          {!isEdit && (
            <div>
              <label className="block mb-1">{t('residents.form.password')}<span className="text-red-500 ml-1">*</span></label>
              <Field as={Input} name="password" type="password" autoComplete="new-password" />
              {errors.password && touched.password && <div className="text-red-500 text-xs mt-1">{t(errors.password)}</div>}
            </div>
          )}
          <div>
            <label className="block mb-1">{t('residents.table.unitNumber')}<span className="text-red-500 ml-1">*</span></label>
            <Field as={Input} name="unitNumber" />
            {errors.unitNumber && touched.unitNumber && <div className="text-red-500 text-xs mt-1">{t(errors.unitNumber)}</div>}
          </div>
          <div>
            <label className="block mb-1">{t('residents.table.unitBlock')}<span className="text-red-500 ml-1">*</span></label>
            <Field as={Input} name="unitBlock" />
            {errors.unitBlock && touched.unitBlock && <div className="text-red-500 text-xs mt-1">{t(errors.unitBlock)}</div>}
          </div>
          <div>
            <label className="block mb-1">{t('residents.table.complex')}<span className="text-red-500 ml-1">*</span></label>
            <Field as="select" name="residentialComplexId" className="w-full border rounded px-2 py-1" disabled={ status!== 'success'}>
              <option value="">{t('residents.form.selectComplex')}</option>
              {complexes && complexes.map(complex => (
                <option key={complex.id} value={complex.id}>{complex.name}</option>
              ))}
            </Field>
            {errors.residentialComplexId && touched.residentialComplexId && (
              <div className="text-red-500 text-xs mt-1">{t(errors.residentialComplexId)}</div>
            )}
          </div>
          <Button type="submit" disabled={isPending || !isValid || (!dirty && submitCount === 0)}>
            {isPending ? t('form.saving') : t(isEdit ? 'form.save' : 'form.create')}
          </Button>
        </Form>
      )}
    </Formik>
  );
}
