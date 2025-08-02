import { useResidentialComplexMutation } from '@/hooks/useResidentialComplexMutation';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { Button } from '../ui/button';
import { Input } from '../ui/input';
import { useTranslation } from 'react-i18next';
import type { ResidentialComplexCommandDto } from '@/api/api';
import { Label } from '../ui/label';

export interface ResidentialComplexFormValues {
  name: string;
  address: string;
  city: string;
  postalCode: string;
}

const validationSchema = Yup.object({
  name: Yup.string().required('residentialComplex.form.nameRequired'),
  address: Yup.string().required('residentialComplex.form.addressRequired'),
  city: Yup.string().required('residentialComplex.form.cityRequired'),
  postalCode: Yup.string().required('residentialComplex.form.postalCodeRequired'),
});





interface Props {
  initialValues: ResidentialComplexCommandDto;
  id?: string;
  onSuccess?: () => void;
  onCancel?: () => void;
}


export function ResidentialComplexForm({ initialValues, id, onSuccess, onCancel }: Props) {
  const { t } = useTranslation();
  const isEdit = id !== undefined;
  const { mutate, isPending } = useResidentialComplexMutation(id, onSuccess);
  return (
    <Formik
      initialValues={initialValues}
      validationSchema={validationSchema}
      onSubmit={mutate}
      enableReinitialize
    >
      {({ errors, touched }) => (
        <Form className="space-y-4">
          <div>
            <Label className="block mb-1" htmlFor="name">
              {t('residentialComplex.form.name')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="name" id="name" />
            {errors.name && touched.name && <div className="text-red-500 text-xs mt-1">{t(errors.name as string)}</div>}
          </div>
          <div>
            <Label className="block mb-1" htmlFor="address">
              {t('residentialComplex.form.address')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="address" id="address" />
            {errors.address && touched.address && <div className="text-red-500 text-xs mt-1">{t(errors.address as string)}</div>}
          </div>
          <div>
            <Label className="block mb-1" htmlFor="city">
              {t('residentialComplex.form.city')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="city" id="city" />
            {errors.city && touched.city && <div className="text-red-500 text-xs mt-1">{t(errors.city as string)}</div>}
          </div>
          <div>
            <Label className="block mb-1" htmlFor="postalCode">
              {t('residentialComplex.form.postalCode')}
              <span className="text-red-500 ml-1">*</span>
            </Label>
            <Field as={Input} name="postalCode" id="postalCode" />
            {errors.postalCode && touched.postalCode && <div className="text-red-500 text-xs mt-1">{t(errors.postalCode as string)}</div>}
          </div>
          <div className="flex gap-2 justify-end">
            {onCancel && (
            <Button type="button" variant="outline" onClick={onCancel} disabled={isPending}>
                {t('form.cancel')}
              </Button>
            )}
            <Button type="submit" disabled={isPending}>
              {isPending ? t('form.saving') : t(isEdit ? 'form.save' : 'form.create')}
            </Button>
          </div>
        </Form>
      )}
    </Formik>
  );
}
