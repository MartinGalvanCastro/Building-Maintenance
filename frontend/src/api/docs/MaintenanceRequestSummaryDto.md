# MaintenanceRequestSummaryDto

Summary information about a maintenance request, including related technician and resident.

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** | Unique identifier of the maintenance request. | [optional] [default to undefined]
**status** | **string** | Status of the maintenance request. | [optional] [default to undefined]
**createdAt** | **string** | Date and time when the request was created. | [optional] [default to undefined]
**description** | **string** | Description of the maintenance request. | [optional] [default to undefined]
**specialization** | **string** | Specialization required for the request. | [optional] [default to undefined]
**scheduledAt** | **string** | Scheduled date and time for the request. | [optional] [default to undefined]
**technician** | [**TechnicianSummaryDto**](TechnicianSummaryDto.md) | Summary of the assigned technician. | [optional] [default to undefined]
**resident** | [**ResidentSummaryDto**](ResidentSummaryDto.md) | Summary of the resident who made the request. | [optional] [default to undefined]

## Example

```typescript
import { MaintenanceRequestSummaryDto } from './api';

const instance: MaintenanceRequestSummaryDto = {
    id,
    status,
    createdAt,
    description,
    specialization,
    scheduledAt,
    technician,
    resident,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
