# LogInResultDto

DTO returned after successful authentication, containing the JWT token.

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**token** | **string** | JWT token for authenticated user. | [optional] [default to undefined]

## Example

```typescript
import { LogInResultDto } from './api';

const instance: LogInResultDto = {
    token,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
