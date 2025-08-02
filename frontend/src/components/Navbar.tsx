import { useAuth } from '@/hooks/useAuth';
import { NavigationMenu, NavigationMenuList, NavigationMenuItem, NavigationMenuLink } from './ui/navigation-menu';
import { Avatar, AvatarImage, AvatarFallback } from './ui/avatar';
import { Sheet, SheetTrigger, SheetContent } from './ui/sheet';
import { Button } from './ui/button';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { Menu } from 'lucide-react';


export function Navbar() {

  const { t } = useTranslation();
  const { tokenPayload, logout, isLoggingOut } = useAuth();
  const navigate = useNavigate();
  const role = tokenPayload?.role;
  const navItems = [
    { label: t('navbar.home'), to: '/home', roles: ['ADMIN', 'TECHNICIAN', 'RESIDENT'] },
    { label: t('navbar.maintenance'), to: '/maintenance-request', roles: ['ADMIN', 'TECHNICIAN', 'RESIDENT'] },
    { label: t('navbar.residentialComplex'), to: '/residential-complex', roles: ['ADMIN'] },
    { label: t('navbar.residents'), to: '/residents', roles: ['ADMIN'] },
    { label: t('navbar.technicians'), to: '/technicians', roles: ['ADMIN'] },
  ];
  const filteredNav = navItems.filter(item => item.roles.includes(role as string));
  const handleLogout = async () => {
    await logout();
  };

  return (
    <nav className="w-full bg-background rounded-xl shadow-xl mt-6 mb-6 mx-auto max-w-5xl">
      <div className="flex items-center justify-between px-4 py-2">
        <div className="flex items-center gap-4">
          <span className="text-lg font-bold tracking-tight">BMA</span>
          <div className="hidden md:flex">
            <NavigationMenu>
              <NavigationMenuList className="gap-2">
                {filteredNav.map(item => (
                  <NavigationMenuItem key={item.to}>
                    <NavigationMenuLink onClick={() => navigate(item.to)}>
                      {item.label}
                    </NavigationMenuLink>
                  </NavigationMenuItem>
                ))}
              </NavigationMenuList>
            </NavigationMenu>
          </div>
        </div>
        <div className="flex items-center gap-4">
          <div className="hidden md:flex items-center gap-2">
            <div className="h-8 border-l border-muted mx-2" />
            <Avatar>
              <AvatarImage src="/avatar.png" alt="avatar" />
              <AvatarFallback>A</AvatarFallback>
            </Avatar>
            <div className="w-2" />
            <Button
              variant="destructive"
              size="sm"
              onClick={handleLogout}
              className={isLoggingOut ? 'animate-pulse' : ''}
              disabled={isLoggingOut}
            >
              {isLoggingOut ? t('navbar.loggingOut') : t('navbar.logout')}
            </Button>
          </div>
          <div className="block md:hidden">
            <Sheet>
              <SheetTrigger asChild>
                <Button variant="ghost" size="icon" aria-label="Menu">
                  <Menu className="h-6 w-6" />
                </Button>
              </SheetTrigger>
              <SheetContent side="right" className="p-0 w-56">
                <NavigationMenu orientation="vertical">
                  <NavigationMenuList className="flex flex-col gap-2 p-4">
                    {filteredNav.map(item => (
                      <NavigationMenuItem key={item.to}>
                        <NavigationMenuLink className="w-full" onClick={() => navigate(item.to)}>
                          {item.label}
                        </NavigationMenuLink>
                      </NavigationMenuItem>
                    ))}
                  </NavigationMenuList>
                </NavigationMenu>
                <div className="flex items-center gap-2 p-4 border-t mt-4">
                  <div className="h-8 border-l border-muted mr-2" />
                  <Avatar>
                    <AvatarImage src="/avatar.png" alt="avatar" />
                    <AvatarFallback>A</AvatarFallback>
                  </Avatar>
                  <div className="w-2" />
                  <Button
                    variant="destructive"
                    size="sm"
                    onClick={handleLogout}
                    className={isLoggingOut ? 'animate-pulse' : ''}
                    disabled={isLoggingOut}
                  >
                    {isLoggingOut ? t('navbar.loggingOut') : t('navbar.logout')}
                  </Button>
                </div>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </div>
    </nav>
  );
}
