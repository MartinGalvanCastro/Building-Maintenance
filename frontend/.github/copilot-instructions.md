All providers should be placed in the `src/providers/` folder for organization.

All API operations should be defined in the `src/services/` folder.

Do not generate comments unless specified. This is the second most important rule. Comments should only be added when explicitly requested

All UI should be responsive.

All router and route components should be placed inside a `src/router/` folder for organization.

Do not create intermediate variables. Always use values directly in expressions, function calls, or JSX unless absolutely necessary for performance. Readability is not a valid reason.

This is a Vite + React + TypeScript project. Use React best practices, organize code into components, hooks, screens, and utils folders. Use react-router for navigation and react-query for data fetching. Use vitest for testing.


For forms, use Formik for form state management and Yup for validation.
For styling, use Tailwind CSS. Do not modify the index.css file for custom styles; use Tailwind utility classes instead. Unless specified

Use named imports for React components and hooks. Use TypeScript types and interfaces for props and state management.

All hooks should be defined in the `src/hooks/` directory. Use custom hooks for shared logic and state management.

All components should be defined in the `src/components/` directory. Use functional components with hooks for state and lifecycle management.

All screens should be defined in the `src/screens/` directory. Use these for top-level pages and routing.

All utility functions should be defined in the `src/utils/` directory. Use these for shared logic that doesn't fit into components or hooks.

When writing tests, use Vitest and Testing Library. Follow the project's testing conventions and ensure tests are placed in the appropriate directories.

All hooks should define a return type for better type safety. Use TypeScript interfaces for props and state management.

Use of any is not allowed, fobidden and the most important rule. Always define specific types for function parameters and return values.

# i18n rules
- Do not use fallback values in any `t()` or i18n function call. All translations must be mapped in `src/i18n.ts`.
- Every UI string must have a corresponding i18n key. Fallbacks (e.g. `t('key', 'Fallback')`) are strictly prohibited.
- All i18n keys must use a flat, dot-notated format (e.g. `table.add`, `form.save`). Do not use nested objects for translation keys. All keys must be at the top level of the translation object.
- Everytime a new i18n is specified in a component, it must be added to the `src/i18n.ts` file with a corresponding translation in the `en` locale.