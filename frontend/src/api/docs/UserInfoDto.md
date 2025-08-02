# UserInfoDto

DTO containing user info, relationships, and related maintenance requests for the /me endpoint.

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** | Unique identifier of the user. | [optional] [default to undefined]
**name** | **string** | Full name of the user. | [optional] [default to undefined]
**email** | **string** | Email address of the user. | [optional] [default to undefined]
**role** | **string** | Role of the user (e.g., RESIDENT, ADMIN, TECHNICIAN). | [optional] [default to undefined]
**residentialComplex** | [**ResidentialComplexDto**](ResidentialComplexDto.md) | Residential complex info (only for residents). | [optional] [default to undefined]
**maintenanceRequests** | [**Array&lt;MaintenanceRequestSummaryDto&gt;**](MaintenanceRequestSummaryDto.md) | List of related maintenance requests. | [optional] [default to undefined]

## Example

```typescript
import { UserInfoDto } from './api';

const instance: UserInfoDto = {
    id,
    name,
    email,
    role,
    residentialComplex,
    maintenanceRequests,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
