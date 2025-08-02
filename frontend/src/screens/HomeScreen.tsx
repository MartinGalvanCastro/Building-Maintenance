import { AuthLayout } from '../components/AuthLayout';
import { useAuth } from '@/hooks/useAuth';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/card';
import { Wrench, Building2, Users, UserCog } from 'lucide-react';
import { useTranslation } from 'react-i18next';

export function HomeScreen() {
  const { t } = useTranslation();
  const { tokenPayload } = useAuth();
  const role = tokenPayload?.role;
  const cards = [
    {
      key: 'home.maintenance',
      title: t('home.maintenance.title'),
      desc: t('home.maintenance.desc'),
      icon: <Wrench className="h-7 w-7 text-primary" />, 
      roles: ['ADMIN', 'TECHNICIAN', 'RESIDENT'],
    },
    {
      key: 'home.residentialComplex',
      title: t('home.residentialComplex.title'),
      desc: t('home.residentialComplex.desc'),
      icon: <Building2 className="h-7 w-7 text-primary" />, 
      roles: ['ADMIN'],
    },
    {
      key: 'home.residents',
      title: t('home.residents.title'),
      desc: t('home.residents.desc'),
      icon: <Users className="h-7 w-7 text-primary" />, 
      roles: ['ADMIN'],
    },
    {
      key: 'home.technicians',
      title: t('home.technicians.title'),
      desc: t('home.technicians.desc'),
      icon: <UserCog className="h-7 w-7 text-primary" />, 
      roles: ['ADMIN'],
    },
  ];
  const filteredCards = cards.filter(card => card.roles.includes(role as string));
  const userName = tokenPayload?.name || t('home.user');
  return (
    <AuthLayout>
      <div className="max-w-4xl mx-auto mt-8 px-4">
        <h1 className="text-2xl font-bold mb-2">{t('home.welcome', { name: userName })}</h1>
        <p className="text-muted-foreground mb-8">{t('home.intro')}</p>
        <div className="grid gap-6 md:grid-cols-2">
          {filteredCards.map(card => (
            <Card key={card.key} className="hover:shadow-lg transition-shadow h-40 flex flex-col items-center justify-center">
              <CardHeader className="flex flex-col items-center justify-center gap-1 pb-1 w-full">
                {card.icon}
                <CardTitle className="mb-1 text-center w-full">{card.title}</CardTitle>
                <CardDescription className="mt-0.5 text-center w-full">{card.desc}</CardDescription>
              </CardHeader>
              <CardContent />
            </Card>
          ))}
        </div>
      </div>
    </AuthLayout>
  );
}
