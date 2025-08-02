# ResidentialComplexManagementApi

All URIs are relative to *http://localhost:8080/api*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**create3**](#create3) | **POST** /residential-complexes | Create a new residential complex|
|[**delete2**](#delete2) | **DELETE** /residential-complexes/{id} | Delete a residential complex|
|[**getOne2**](#getone2) | **GET** /residential-complexes/{id} | Get a residential complex by ID|
|[**listAll2**](#listall2) | **GET** /residential-complexes | List all residential complexes|
|[**update2**](#update2) | **PUT** /residential-complexes/{id} | Update a residential complex|

# **create3**
> ResidentialComplexDto create3(residentialComplexCommandDto)

Creates a new residential complex with the provided details.

### Example

```typescript
import {
    ResidentialComplexManagementApi,
    Configuration,
    ResidentialComplexCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentialComplexManagementApi(configuration);

let residentialComplexCommandDto: ResidentialComplexCommandDto; //Residential complex creation data

const { status, data } = await apiInstance.create3(
    residentialComplexCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentialComplexCommandDto** | **ResidentialComplexCommandDto**| Residential complex creation data | |


### Return type

**ResidentialComplexDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Residential complex created |  -  |
|**400** | Invalid input data |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **delete2**
> DeleteResponseDto delete2()

Deletes a residential complex by its ID.

### Example

```typescript
import {
    ResidentialComplexManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentialComplexManagementApi(configuration);

let id: string; //UUID of the residential complex (default to undefined)

const { status, data } = await apiInstance.delete2(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the residential complex | defaults to undefined|


### Return type

**DeleteResponseDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Residential complex deleted |  -  |
|**404** | Residential complex not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getOne2**
> ResidentialComplexDto getOne2()

Returns a residential complex by its unique identifier.

### Example

```typescript
import {
    ResidentialComplexManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentialComplexManagementApi(configuration);

let id: string; //UUID of the residential complex (default to undefined)

const { status, data } = await apiInstance.getOne2(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] | UUID of the residential complex | defaults to undefined|


### Return type

**ResidentialComplexDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Residential complex found |  -  |
|**404** | Residential complex not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **listAll2**
> Array<ResidentialComplexDto> listAll2()

Returns a list of all residential complexes.

### Example

```typescript
import {
    ResidentialComplexManagementApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentialComplexManagementApi(configuration);

const { status, data } = await apiInstance.listAll2();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ResidentialComplexDto>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | List of residential complexes |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **update2**
> ResidentialComplexDto update2(residentialComplexCommandDto)

Updates an existing residential complex by its ID.

### Example

```typescript
import {
    ResidentialComplexManagementApi,
    Configuration,
    ResidentialComplexCommandDto
} from './api';

const configuration = new Configuration();
const apiInstance = new ResidentialComplexManagementApi(configuration);

let id: string; //UUID of the residential complex (default to undefined)
let residentialComplexCommandDto: ResidentialComplexCommandDto; //Residential complex update data

const { status, data } = await apiInstance.update2(
    id,
    residentialComplexCommandDto
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **residentialComplexCommandDto** | **ResidentialComplexCommandDto**| Residential complex update data | |
| **id** | [**string**] | UUID of the residential complex | defaults to undefined|


### Return type

**ResidentialComplexDto**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json, */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Residential complex updated |  -  |
|**400** | Invalid input data |  -  |
|**404** | Residential complex not found |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

