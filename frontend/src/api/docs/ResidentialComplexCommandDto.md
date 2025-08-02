# ResidentialComplexCommandDto

DTO used to create or update a residential complex.

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **string** | Name of the residential complex. | [default to undefined]
**address** | **string** | Address of the residential complex. | [default to undefined]
**city** | **string** | City where the residential complex is located. | [default to undefined]
**postalCode** | **string** | Postal code of the residential complex. | [default to undefined]

## Example

```typescript
import { ResidentialComplexCommandDto } from './api';

const instance: ResidentialComplexCommandDto = {
    name,
    address,
    city,
    postalCode,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
