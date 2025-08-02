# Building Maintenance App Frontend

This project is a React application bootstrapped with [Vite](https://vitejs.dev/) and TypeScript.

## Features
- ⚛️ React 19 + TypeScript
- ⚡ Vite for fast development
- 🧪 Vitest for unit and component testing
- 🧭 React Router for navigation
- 🔗 React Query for data fetching and caching
- 📁 Organized folder structure: `components`, `hooks`, `screens`, `utils`

## Getting Started

### Install dependencies
```sh
yarn
```

### Start the development server
```sh
yarn dev
```

### Run tests
```sh
yarn test
```

## Project Structure
- `src/components/` – Reusable UI components
- `src/hooks/` – Custom React hooks
- `src/screens/` – Top-level pages/screens for routing
- `src/utils/` – Utility functions

## Testing
- Uses [Vitest](https://vitest.dev/) and [Testing Library](https://testing-library.com/)
- Test setup file: `src/setupTests.ts`

---

For more details, see the Vite and React documentation.

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default tseslint.config([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
