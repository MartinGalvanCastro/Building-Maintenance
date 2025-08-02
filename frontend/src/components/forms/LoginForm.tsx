import { useAuth } from '../../hooks/useAuth';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '../ui/card';
import { Input } from '../ui/input';
import { Label } from '../ui/label';
import { Button } from '../ui/button';
import { useTranslation } from 'react-i18next';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';


export function LoginForm() {
  const { t } = useTranslation();
  const { login, isLoggingIn: isLoading, loginError: error } = useAuth();

  const validationSchema = Yup.object({
    email: Yup.string()
      .email(t('login.form.emailInvalid'))
      .required(t('login.form.emailRequired')),
    password: Yup.string()
      .min(6, t('login.form.passwordMin'))
      .required(t('login.form.passwordRequired')),
  });

  return (
    <div className="flex items-center justify-center min-h-screen">
      <Card className="w-full max-w-sm">
        <CardHeader className="text-center space-y-1 mb-2">
          <CardTitle className="text-2xl font-bold tracking-tight">{t('app.name')}</CardTitle>
          <div className="text-xl font-semibold">{t('login.form.title')}</div>
          <CardDescription>{t('login.form.description')}</CardDescription>
        </CardHeader>
        <Formik
          initialValues={{ email: '', password: '' }}
          validationSchema={validationSchema}
          onSubmit={async ({ email, password }, { setSubmitting }) => {
            try {
              await login({ email, password });
            } finally {
              setSubmitting(false);
            }
          }}
        >
          {({ isSubmitting }) => (
            <Form>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="email">{t('login.form.emailLabel')}</Label>
                  <Field
                    as={Input}
                    id="email"
                    name="email"
                    type="email"
                    autoFocus
                    placeholder={t('login.form.emailPlaceholder')}
                  />
                  <ErrorMessage name="email">
                    {msg => <div className="text-destructive text-xs">{msg}</div>}
                  </ErrorMessage>
                </div>
                <div className="space-y-2 mb-4">
                  <Label htmlFor="password">{t('login.form.passwordLabel')}</Label>
                  <Field
                    as={Input}
                    id="password"
                    name="password"
                    type="password"
                    placeholder={t('login.form.passwordPlaceholder')}
                  />
                  <ErrorMessage name="password">
                    {msg => <div className="text-destructive text-xs">{msg}</div>}
                  </ErrorMessage>
                </div>
                {!!error && <div className="text-destructive text-sm">{t('login.form.error')}</div>}
              </CardContent>
              <CardFooter>
                <Button type="submit" className="w-full" disabled={isLoading || isSubmitting}>
                  {isLoading || isSubmitting ? t('login.form.loading') : t('login.form.submit')}
                </Button>
              </CardFooter>
            </Form>
          )}
        </Formik>
      </Card>
    </div>
  );
}
